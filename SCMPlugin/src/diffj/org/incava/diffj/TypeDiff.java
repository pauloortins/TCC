package diffj.org.incava.diffj;

import java.io.*;
import java.util.*;

import diffj.net.sourceforge.pmd.ast.*;
import diffj.org.incava.analysis.*;
import diffj.org.incava.java.*;
import diffj.org.incava.lang.*;
import diffj.org.incava.util.TimedEvent;
import dubaj.tr.MudancaVO;
import dubaj.tr.Relatorio;
import dubaj.tr.Relatorio.TipoDeclaracao;
import dubaj.tr.contagem.metodo.MudancaMetodo;
import dubaj.tr.contagem.metodo.MudancaMetodoAdicao;
import dubaj.tr.contagem.metodo.MudancaMetodoRemocao;

public class TypeDiff extends ItemDiff {
	public static final String TYPE_CHANGED_FROM_CLASS_TO_INTERFACE = "type changed from class to interface";

	public static final String TYPE_CHANGED_FROM_INTERFACE_TO_CLASS = "type changed from interface to class";

	public static final String METHOD_REMOVED = "method removed: {0}";

	public static final String METHOD_ADDED = "method added: {0}";

	public static final String METHOD_CHANGED = "method changed from {0} to {1}";

	public static final String CONSTRUCTOR_REMOVED = "constructor removed: {0}";

	public static final String CONSTRUCTOR_ADDED = "constructor added: {0}";

	public static final String FIELD_REMOVED = "field removed: {0}";

	public static final String FIELD_ADDED = "field added: {0}";

	public static final String INNER_INTERFACE_ADDED = "inner interface added: {0}";

	public static final String INNER_INTERFACE_REMOVED = "inner interface removed: {0}";

	public static final String INNER_CLASS_ADDED = "inner class added: {0}";

	public static final String INNER_CLASS_REMOVED = "inner class removed: {0}";

	public static final String EXTENDED_TYPE_REMOVED = "extended type removed: {0}";

	public static final String EXTENDED_TYPE_ADDED = "extended type added: {0}";

	public static final String EXTENDED_TYPE_CHANGED = "extended type changed from {0} to {1}";

	public static final String IMPLEMENTED_TYPE_REMOVED = "implemented type removed: {0}";

	public static final String IMPLEMENTED_TYPE_ADDED = "implemented type added: {0}";

	public static final String IMPLEMENTED_TYPE_CHANGED = "implemented type changed from {0} to {1}";

	public static final int[] VALID_TYPE_MODIFIERS = new int[] {
			JavaParserConstants.ABSTRACT, JavaParserConstants.FINAL,
			JavaParserConstants.STATIC, // valid only for inner types
			JavaParserConstants.STRICTFP };

	public TypeDiff(Report report) {
		super(report);
	}

	public TypeDiff(Collection differences) {
		super(differences);
	}

	public void compare(SimpleNode aType, SimpleNode bType) {
		dubaj.tr.Ace.log("aType", aType);
		dubaj.tr.Ace.log("bType", bType);

		// should have only one child, the type itself, either an interface or a
		// class declaration

		ASTTypeDeclaration a = (ASTTypeDeclaration) aType;
		ASTTypeDeclaration b = (ASTTypeDeclaration) bType;

		compare(a, b);
	}

	public void compare(ASTTypeDeclaration a, ASTTypeDeclaration b) {
		dubaj.tr.Ace.log("a", a);
		dubaj.tr.Ace.log("b", b);

		// should have only one child, the type itself, either an interface or a
		// class declaration

		ASTClassOrInterfaceDeclaration at = TypeDeclarationUtil.getType(a);
		ASTClassOrInterfaceDeclaration bt = TypeDeclarationUtil.getType(b);

		dubaj.tr.Ace.log("at", at);
		dubaj.tr.Ace.log("bt", bt);

		if (at == null && bt == null) {
			dubaj.tr.Ace.log("skipping 'semicolon declarations'");
		} else {
			compare(at, bt);
		}
	}

	public void compare(ASTClassOrInterfaceDeclaration at,
			ASTClassOrInterfaceDeclaration bt) {
		// SimpleNodeUtil.dump(at, "a");
		// SimpleNodeUtil.dump(bt, "b");

		if (at.isInterface() == bt.isInterface()) {
			dubaj.tr.Ace.log("no change in types");
		} else if (bt.isInterface()) {
			addChanged(TYPE_CHANGED_FROM_CLASS_TO_INTERFACE, null, at, bt,
					Relatorio.TipoDeclaracao.DeclaracaoClasse);
		} else {
			addChanged(TYPE_CHANGED_FROM_INTERFACE_TO_CLASS, null, at, bt,
					Relatorio.TipoDeclaracao.DeclaracaoClasse);
		}

		SimpleNode atParent = SimpleNodeUtil.getParent(at);
		SimpleNode btParent = SimpleNodeUtil.getParent(bt);

		if (compararAcessoMetodo(atParent, btParent, Relatorio.TipoDeclaracao.Classe))
		{
			Relatorio.getMudancaClasse().incrementarMudancasClasse();
		}

		if (compararModificadorMetodo(atParent, btParent, VALID_TYPE_MODIFIERS,
				Relatorio.TipoDeclaracao.Classe))
		{
			Relatorio.getMudancaClasse().incrementarMudancasClasse();
		}

		compareExtends(at, bt);
		compareImplements(at, bt);

		compareDeclarations(at, bt);
	}

	protected void addAllDeclarations(
			ASTClassOrInterfaceBodyDeclaration[] decls,
			ASTClassOrInterfaceDeclaration other, boolean added) {
		for (int di = 0; di < decls.length; ++di) {
			addDeclaration(decls[di], other, added);
		}
	}

	protected Map getExtImpMap(ASTClassOrInterfaceDeclaration coid,
			Class extImpClass) {
		Map map = new HashMap();
		SimpleNode list = SimpleNodeUtil.findChild(coid, extImpClass);

		if (list == null) {
			// tr.Ace.log("no list");
		} else {
			ASTClassOrInterfaceType[] types = (ASTClassOrInterfaceType[]) SimpleNodeUtil
					.findChildren(list, ASTClassOrInterfaceType.class);

			for (int ti = 0; ti < types.length; ++ti) {
				ASTClassOrInterfaceType type = types[ti];
				String typeName = SimpleNodeUtil.toString(type);
				map.put(typeName, type);
			}
		}

		return map;
	}
	
	protected boolean compararHeranca(ASTClassOrInterfaceDeclaration at,
			ASTClassOrInterfaceDeclaration bt, String addMsg, String chgMsg,
			String delMsg, Class extImpCls,
			Relatorio.TipoDeclaracao tipoElemento) {
		Map aMap = getExtImpMap(at, extImpCls);
		Map bMap = getExtImpMap(bt, extImpCls);

		/*
		 * Compara as implementacoes de interface
		 */

		// Nesse caso uma mudanca em 2 ou mais declaracoes de heranca iriam
		// contar
		// mais de uma vez, logo eu coloco um return para garantir que só será
		// contada uma vez

		// tr.Ace.log("aMap", aMap);
		// tr.Ace.log("bMap", bMap);

		// I don't like this special case, but it is better than two separate
		// "add" and "remove" messages.

		if (aMap.size() == 1 && bMap.size() == 1) {
			String aName = (String) aMap.keySet().iterator().next();
			String bName = (String) bMap.keySet().iterator().next();

			if (aName.equals(bName)) {
				// tr.Ace.log("no change");
			} else {
				ASTClassOrInterfaceType a = (ASTClassOrInterfaceType) aMap
						.get(aName);
				ASTClassOrInterfaceType b = (ASTClassOrInterfaceType) bMap
						.get(bName);

				addChanged(chgMsg, new Object[] { aName, bName }, a, b,
						tipoElemento);
				return true;
			}
		} else {
			List typeNames = new ArrayList();
			typeNames.addAll(aMap.keySet());
			typeNames.addAll(bMap.keySet());

			dubaj.tr.Ace.log("typeNames", typeNames);

			Iterator tit = typeNames.iterator();
			while (tit.hasNext()) {
				String typeName = (String) tit.next();

				ASTClassOrInterfaceType aType = (ASTClassOrInterfaceType) aMap
						.get(typeName);
				ASTClassOrInterfaceType bType = (ASTClassOrInterfaceType) bMap
						.get(typeName);

				if (aType == null) {
					addChanged(addMsg, new Object[] { typeName }, at, bType,
							tipoElemento);

					return true;
				} else if (bType == null) {
					addChanged(delMsg, new Object[] { typeName }, aType, bt,
							tipoElemento);

					return true;

				} else {
					// tr.Ace.log("no change");
				}
			}
		}
		
		return false;
	}
	
	protected boolean compararInterface(ASTClassOrInterfaceDeclaration at,
			ASTClassOrInterfaceDeclaration bt, String addMsg, String chgMsg,
			String delMsg, Class extImpCls,
			Relatorio.TipoDeclaracao tipoElemento) {
		Map aMap = getExtImpMap(at, extImpCls);
		Map bMap = getExtImpMap(bt, extImpCls);

		/*
		 * Compara as implementacoes de interface
		 */

		// Nesse caso uma mudanca em 2 ou mais declaracoes de heranca iriam
		// contar
		// mais de uma vez, logo eu coloco um return para garantir que só será
		// contada uma vez

		// tr.Ace.log("aMap", aMap);
		// tr.Ace.log("bMap", bMap);

		// I don't like this special case, but it is better than two separate
		// "add" and "remove" messages.

		if (aMap.size() == 1 && bMap.size() == 1) {
			String aName = (String) aMap.keySet().iterator().next();
			String bName = (String) bMap.keySet().iterator().next();

			if (aName.equals(bName)) {
				// tr.Ace.log("no change");
			} else {
				ASTClassOrInterfaceType a = (ASTClassOrInterfaceType) aMap
						.get(aName);
				ASTClassOrInterfaceType b = (ASTClassOrInterfaceType) bMap
						.get(bName);

				addChanged(chgMsg, new Object[] { aName, bName }, a, b,
						tipoElemento);

				return true;
			}
		} else {
			List typeNames = new ArrayList();
			typeNames.addAll(aMap.keySet());
			typeNames.addAll(bMap.keySet());

			dubaj.tr.Ace.log("typeNames", typeNames);

			Iterator tit = typeNames.iterator();
			while (tit.hasNext()) {
				String typeName = (String) tit.next();

				ASTClassOrInterfaceType aType = (ASTClassOrInterfaceType) aMap
						.get(typeName);
				ASTClassOrInterfaceType bType = (ASTClassOrInterfaceType) bMap
						.get(typeName);

				if (aType == null) {
					addChanged(addMsg, new Object[] { typeName }, at, bType,
							tipoElemento);

					return true;
				} else if (bType == null) {
					addChanged(delMsg, new Object[] { typeName }, aType, bt,
							tipoElemento);
					
					return true;

				} else {
					// tr.Ace.log("no change");
				}
			}
		}
		
		return false;
	}

	protected void compareImpExt(ASTClassOrInterfaceDeclaration at,
			ASTClassOrInterfaceDeclaration bt, String addMsg, String chgMsg,
			String delMsg, Class extImpCls,
			Relatorio.TipoDeclaracao tipoElemento) {
		Map aMap = getExtImpMap(at, extImpCls);
		Map bMap = getExtImpMap(bt, extImpCls);

		/*
		 * Compara as implementacoes de interface
		 */

		// Nesse caso uma mudanca em 2 ou mais declaracoes de heranca iriam
		// contar
		// mais de uma vez, logo eu coloco um return para garantir que só será
		// contada uma vez

		// tr.Ace.log("aMap", aMap);
		// tr.Ace.log("bMap", bMap);

		// I don't like this special case, but it is better than two separate
		// "add" and "remove" messages.

		if (aMap.size() == 1 && bMap.size() == 1) {
			String aName = (String) aMap.keySet().iterator().next();
			String bName = (String) bMap.keySet().iterator().next();

			if (aName.equals(bName)) {
				// tr.Ace.log("no change");
			} else {
				ASTClassOrInterfaceType a = (ASTClassOrInterfaceType) aMap
						.get(aName);
				ASTClassOrInterfaceType b = (ASTClassOrInterfaceType) bMap
						.get(bName);

				addChanged(chgMsg, new Object[] { aName, bName }, a, b,
						tipoElemento);

				return;
			}
		} else {
			List typeNames = new ArrayList();
			typeNames.addAll(aMap.keySet());
			typeNames.addAll(bMap.keySet());

			dubaj.tr.Ace.log("typeNames", typeNames);

			Iterator tit = typeNames.iterator();
			while (tit.hasNext()) {
				String typeName = (String) tit.next();

				ASTClassOrInterfaceType aType = (ASTClassOrInterfaceType) aMap
						.get(typeName);
				ASTClassOrInterfaceType bType = (ASTClassOrInterfaceType) bMap
						.get(typeName);

				if (aType == null) {
					addChanged(addMsg, new Object[] { typeName }, at, bType,
							tipoElemento);

					return;
				} else if (bType == null) {
					addChanged(delMsg, new Object[] { typeName }, aType, bt,
							tipoElemento);

					return;

				} else {
					// tr.Ace.log("no change");
				}
			}
		}
	}

	protected void compareExtends(ASTClassOrInterfaceDeclaration at,
			ASTClassOrInterfaceDeclaration bt) {
		if (compararHeranca(at, bt, EXTENDED_TYPE_ADDED, EXTENDED_TYPE_CHANGED,
				EXTENDED_TYPE_REMOVED, ASTExtendsList.class,
				Relatorio.TipoDeclaracao.Heranca))
		{
			Relatorio.getMudancaClasse().incrementarMudancasClasse();
		}
	}

	protected void compareImplements(ASTClassOrInterfaceDeclaration at,
			ASTClassOrInterfaceDeclaration bt) {
		if (compararInterface(at, bt, IMPLEMENTED_TYPE_ADDED, IMPLEMENTED_TYPE_CHANGED,
				IMPLEMENTED_TYPE_REMOVED, ASTImplementsList.class,
				Relatorio.TipoDeclaracao.Interface))
				{
					Relatorio.getMudancaClasse().incrementarMudancasClasse();
				}
	}

	protected void addDeclaration(boolean isAdded, String addMsg,
			String remMsg, String name, SimpleNode from, SimpleNode to,
			Relatorio.TipoDeclaracao tipoElemento) {
		String msg = null;
		String type = null;
		if (isAdded) {
			msg = addMsg;
			type = CodeReference.ADDED;
		} else {
			msg = remMsg;
			SimpleNode x = from;
			from = to;
			to = x;
			type = CodeReference.DELETED;
		}

		add(type, msg, new Object[] { name }, from, to, tipoElemento);
	}

	protected void addDeclaration(SimpleNode decl, SimpleNode other,
			boolean added) {
		dubaj.tr.Ace.log("decl: " + decl);

		if (decl instanceof ASTClassOrInterfaceBodyDeclaration) {
			decl = TypeDeclarationUtil
					.getDeclaration((ASTClassOrInterfaceBodyDeclaration) decl);
		}

		if (decl instanceof ASTMethodDeclaration) {
			ASTMethodDeclaration method = (ASTMethodDeclaration) decl;
			String fullName = MethodUtil.getFullName(method);
			addDeclaration(added, METHOD_ADDED, METHOD_REMOVED, fullName,
					other, method, Relatorio.TipoDeclaracao.Metodo);
			if (added) {
				Relatorio.getMudancaClasse().incrementarMudancasMetodo(new MudancaMetodoAdicao(method));
			} else {
				Relatorio.getMudancaClasse().incrementarMudancasMetodo(new MudancaMetodoRemocao(method));
			}
			

		} else if (decl instanceof ASTFieldDeclaration) {
			ASTFieldDeclaration field = (ASTFieldDeclaration) decl;
			String names = FieldUtil.getNames(field);
			addDeclaration(added, FIELD_ADDED, FIELD_REMOVED, names, other,
					field, Relatorio.TipoDeclaracao.Atributo);
			
			if (added) {
				Relatorio.getMudancaClasse().incrementarMudancasAtributo();
			}
			else
			{
				Relatorio.getMudancaClasse().incrementarMudancasAtributo();
			}
			
		} else if (decl instanceof ASTConstructorDeclaration) {
			ASTConstructorDeclaration ctor = (ASTConstructorDeclaration) decl;
			String fullName = CtorUtil.getFullName(ctor);
			addDeclaration(added, CONSTRUCTOR_ADDED, CONSTRUCTOR_REMOVED,
					fullName, other, ctor, Relatorio.TipoDeclaracao.Construtor);
		} else if (decl instanceof ASTClassOrInterfaceDeclaration) {
			ASTClassOrInterfaceDeclaration coid = (ASTClassOrInterfaceDeclaration) decl;
			String name = ClassUtil.getName(coid).image;
			String addMsg = null;
			String remMsg = null;
			if (coid.isInterface()) {
				addMsg = INNER_INTERFACE_ADDED;
				remMsg = INNER_INTERFACE_REMOVED;
			} else {
				addMsg = INNER_CLASS_ADDED;
				remMsg = INNER_CLASS_REMOVED;
			}
			addDeclaration(added, addMsg, remMsg, name, other, coid,
					Relatorio.TipoDeclaracao.DeclaracaoClasse);
		} else if (decl == null) {
			// nothing.
		} else {
			dubaj.tr.Ace.stack(dubaj.tr.Ace.REVERSE, "WTF? decl: " + decl);
		}
	}

	/**
	 * Adds the declarations in <code>declared</code> that are not in the
	 * <code>compared</code> list. <code>isAdd</code> denotes whether the
	 * declarations are added or removed.
	 */
	protected void addDeclarations(SimpleNode[] declared, List compared,
			SimpleNode other, boolean isAdd) {
		for (int di = 0; di < declared.length; ++di) {
			if (!compared.contains(declared[di])) {
				addDeclaration(declared[di], other, isAdd);
			}
		}
	}

	protected void compareDeclarations(ASTClassOrInterfaceDeclaration aNode,
			ASTClassOrInterfaceDeclaration bNode) {
		// now the children, below the modifiers in the AST.

		ASTClassOrInterfaceBodyDeclaration[] aDecls = TypeDeclarationUtil
				.getDeclarations(aNode);
		ASTClassOrInterfaceBodyDeclaration[] bDecls = TypeDeclarationUtil
				.getDeclarations(bNode);

		// compare declarations in A against B

		if (aDecls.length == 0) {
			if (bDecls.length == 0) {
				// tr.Ace.log("no change (both of zero length)");
			} else {
				addAllDeclarations(bDecls, aNode, true);
			}
		} else if (bDecls.length == 0) {
			addAllDeclarations(aDecls, bNode, false);
		} else {
			MethodUtil methodUtil = new MethodUtil();
			Map matches = TypeDeclarationUtil.matchDeclarations(aDecls, bDecls,
					methodUtil);
			Iterator sit = matches.keySet().iterator();
			List aSeen = new ArrayList();
			List bSeen = new ArrayList();

			// TimedEvent dte = new TimedEvent("diffs");

			Collection diffs = get();

			while (sit.hasNext()) {
				Double dScore = (Double) sit.next();
				List atScore = (List) matches.get(dScore);

				Iterator vit = atScore.iterator();
				while (vit.hasNext()) {
					Object[] values = (Object[]) vit.next();

					ASTClassOrInterfaceBodyDeclaration aDecl = (ASTClassOrInterfaceBodyDeclaration) values[0];
					ASTClassOrInterfaceBodyDeclaration bDecl = (ASTClassOrInterfaceBodyDeclaration) values[1];

					aSeen.add(aDecl);
					bSeen.add(bDecl);

					dubaj.tr.Ace.log("aDecl", aDecl);
					dubaj.tr.Ace.log("bDecl", bDecl);

					SimpleNode ad = TypeDeclarationUtil.getDeclaration(aDecl);
					SimpleNode bd = TypeDeclarationUtil.getDeclaration(bDecl);

					// TODO: Adiciona Mudanca do Metodo
					// we know that the classes of aDecl and bDecl are the same
					if (ad instanceof ASTMethodDeclaration) {
						MethodDiff differ = new MethodDiff(diffs);

						boolean achouMudanca = false;

						if (differ.compararAcessoMetodo(aDecl, bDecl,
								Relatorio.TipoDeclaracao.Metodo)) {
							Relatorio.getMudancaClasse().incrementarMudancasMetodo(new MudancaMetodo());
						}

						if (differ.comparar((ASTMethodDeclaration) ad,
								(ASTMethodDeclaration) bd,
								Relatorio.TipoDeclaracao.Metodo)) {
							
						}

					} else if (ad instanceof ASTFieldDeclaration) {
						FieldDiff differ = new FieldDiff(diffs);
						
						if (differ.compararAcessoMetodo(aDecl, bDecl,
								Relatorio.TipoDeclaracao.Classe))
						{
							Relatorio.getMudancaClasse().incrementarMudancasAtributo();
						}
						
						differ.compare((ASTFieldDeclaration) ad,
								(ASTFieldDeclaration) bd,
								Relatorio.TipoDeclaracao.Classe);
					} else if (ad instanceof ASTConstructorDeclaration) {
						CtorDiff differ = new CtorDiff(diffs);

						boolean achouMudanca = false;

						if (differ.compararAcessoMetodo(aDecl, bDecl,
								Relatorio.TipoDeclaracao.Construtor)) {
							achouMudanca = true;
						}

						if (differ.comparar((ASTConstructorDeclaration) ad,
								(ASTConstructorDeclaration) bd,
								Relatorio.TipoDeclaracao.Construtor)) {
							achouMudanca = true;
						}

						if (achouMudanca) {

							String nomeAntigo = CtorUtil
									.getFullName((ASTConstructorDeclaration) ad);
							String nomeNovo = CtorUtil
									.getFullName((ASTConstructorDeclaration) bd);

						}

					} else if (ad instanceof ASTClassOrInterfaceDeclaration) {
						// access comparison is done in TypeDiff; confusing.
						compare((ASTClassOrInterfaceDeclaration) ad,
								(ASTClassOrInterfaceDeclaration) bd);
					} else if (ad == null && bd == null) {
						// a match between semicolons.
						dubaj.tr.Ace.log("matched 'semicolon declarations'");
					} else {
						dubaj.tr.Ace.reverse("WTF? aDecl: " + ad.getClass());
					}
				}
			}

			// dte.close();

			addDeclarations(aDecls, aSeen, bNode, false);
			addDeclarations(bDecls, bSeen, aNode, true);
		}
	}

}
