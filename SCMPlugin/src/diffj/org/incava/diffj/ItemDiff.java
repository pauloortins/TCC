package diffj.org.incava.diffj;

import java.awt.Point;
import java.text.MessageFormat;
import java.util.*;

import diffj.net.sourceforge.pmd.ast.SimpleNode;
import diffj.net.sourceforge.pmd.ast.Token;
import diffj.org.incava.analysis.*;
import diffj.org.incava.java.ItemUtil;
import diffj.org.incava.java.SimpleNodeUtil;
import diffj.org.incava.qualog.Qualog;
import diffj.org.incava.util.*;
import diffj.org.incava.util.diff.*;
import dubaj.tr.MudancaVO;
import dubaj.tr.Relatorio;
import dubaj.tr.Relatorio.TipoDeclaracao;


public class ItemDiff extends DiffComparator {
	public static final String MODIFIER_REMOVED = "modifier removed: {0}";

	public static final String MODIFIER_ADDED = "modifier added: {0}";

	public static final String MODIFIER_CHANGED = "modifier changed from {0} to {1}";

	public static final String ACCESS_REMOVED = "access removed: {0}";

	public static final String ACCESS_ADDED = "access added: {0}";

	public static final String ACCESS_CHANGED = "access changed from {0} to {1}";

	public static final String CODE_CHANGED = "code changed in {0}";

	public static final String CODE_ADDED = "code added in {0}";

	public static final String CODE_REMOVED = "code removed in {0}";
	
	public static final String RETURN_TYPE_CHANGED = "return type changed from {0} to {1}";

	public ItemDiff(Report report) {
		super(report);
	}

	public ItemDiff(Collection differences) {
		super(differences);
	}
	
	protected boolean compararTiposRetorno(SimpleNode a, SimpleNode b,Relatorio.TipoDeclaracao tipoElemento)
    {
        SimpleNode art    = (SimpleNode)a.jjtGetChild(0);
        SimpleNode brt    = (SimpleNode)b.jjtGetChild(0);
        String     artStr = SimpleNodeUtil.toString(art);
        String     brtStr = SimpleNodeUtil.toString(brt);
        // tr.Ace.log("art: " + art + "; brt: " + brt);

        if (artStr.equals(brtStr)) {
            // tr.Ace.log("no change in return types");
        	return false;
        }
        else {
            addChanged(RETURN_TYPE_CHANGED, new Object[] { artStr, brtStr }, art, brt, tipoElemento);
            return true;
        }
    }

	public void compareModifiers(SimpleNode aNode, SimpleNode bNode,
			int modifierType, Relatorio.TipoDeclaracao tipoElemento) {
		Token aMod = SimpleNodeUtil.getLeadingToken(aNode, modifierType);
		Token bMod = SimpleNodeUtil.getLeadingToken(bNode, modifierType);

		if (aMod == null) {
			if (bMod == null) {
				// no change
			} else {
				addChanged(MODIFIER_ADDED, new Object[] { bMod.image },
						aNode.getFirstToken(), bMod, tipoElemento);
			}
		} else if (bMod == null) {
			addChanged(MODIFIER_REMOVED, new Object[] { aMod.image }, aMod,
					bNode.getFirstToken(), tipoElemento);
		} else if (aMod.kind == bMod.kind) {
			// no change (in modifier type)
		} else {
			addChanged(MODIFIER_ADDED, aMod, bMod, tipoElemento);
		}
	}

	/**
	 * Returns a map from token types ("kinds", as java.lang.Integers), to the
	 * token. This assumes that there are no leadking tokens of the same type
	 * for the given node.
	 */
	protected Map getModifierMap(SimpleNode node) {
		List mods = SimpleNodeUtil.getLeadingTokens(node);
		Map byKind = new HashMap();

		Iterator mit = mods.iterator();
		while (mit.hasNext()) {
			Token tk = (Token) mit.next();
			byKind.put(new Integer(tk.kind), tk);
		}

		return byKind;
	}

	public boolean compareModifiers(SimpleNode aNode, SimpleNode bNode,
			int[] modifierTypes, Relatorio.TipoDeclaracao tipoElemento) {
		List aMods = SimpleNodeUtil.getLeadingTokens(aNode);
		List bMods = SimpleNodeUtil.getLeadingTokens(bNode);

		/*
		 * Compara os modificadores de classe ( Exemplo : final , static e etc )
		 */
		
		// Nesse caso pode ocorrer varias modificacoes nos modificadores
		// logo eu coloco um return para só contar uma vez

		Map aByKind = getModifierMap(aNode);
		Map bByKind = getModifierMap(bNode);

		for (int mi = 0; mi < modifierTypes.length; ++mi) {
			Integer modInt = new Integer(modifierTypes[mi]);

			Token aMod = (Token) aByKind.get(modInt);
			Token bMod = (Token) bByKind.get(modInt);

			if (aMod == null) {
				if (bMod == null) {
					// no change
					return false;
				} else {
					
					addChanged(MODIFIER_ADDED, new Object[] { bMod.image },
							aNode.getFirstToken(), bMod, tipoElemento);
					
					return true;
				}
			} else if (bMod == null) {
				
				addChanged(MODIFIER_REMOVED, new Object[] { aMod.image }, aMod,
						bNode.getFirstToken(), tipoElemento);
				
				return true;
			} else if (aMod.kind == bMod.kind) {
				// no change (in modifier type)
				return false;
			} else {
				dubaj.tr.Ace.red("aMod", aMod);
				dubaj.tr.Ace.red("bMod", bMod);
				
				addChanged(MODIFIER_ADDED, aMod, bMod, tipoElemento);
				return true;
			}
		}
		
		return false;
	}

	public boolean compararModificadorMetodo(SimpleNode aNode,
			SimpleNode bNode, int[] modifierTypes,
			Relatorio.TipoDeclaracao tipoElemento) {
		List aMods = SimpleNodeUtil.getLeadingTokens(aNode);
		List bMods = SimpleNodeUtil.getLeadingTokens(bNode);

		/*
		 * Compara os modificadores de classe ( Exemplo : final , static e etc )
		 */

		Map aByKind = getModifierMap(aNode);
		Map bByKind = getModifierMap(bNode);

		for (int mi = 0; mi < modifierTypes.length; ++mi) {
			Integer modInt = new Integer(modifierTypes[mi]);

			Token aMod = (Token) aByKind.get(modInt);
			Token bMod = (Token) bByKind.get(modInt);

			if (aMod == null) {
				if (bMod == null) {
					// no change
					
				} else {
					addChanged(MODIFIER_ADDED, new Object[] { bMod.image },
							aNode.getFirstToken(), bMod, tipoElemento);
					return true;
				}
			} else if (bMod == null) {
				addChanged(MODIFIER_REMOVED, new Object[] { aMod.image }, aMod,
						bNode.getFirstToken(), tipoElemento);
				return true;
			} else if (aMod.kind == bMod.kind) {
				// no change (in modifier type)
				
			} else {
				dubaj.tr.Ace.red("aMod", aMod);
				dubaj.tr.Ace.red("bMod", bMod);
				addChanged(MODIFIER_ADDED, aMod, bMod, tipoElemento);
				return true;
			}
		}

		return false;
	}

	public boolean compararAcessoMetodo(SimpleNode aNode, SimpleNode bNode,
			Relatorio.TipoDeclaracao tipoElemento) {
		Token aAccess = ItemUtil.getAccess(aNode);
		Token bAccess = ItemUtil.getAccess(bNode);

		/*
		 * Compara os modificadores de acesso pra verificar se foi adicionado ,
		 * removido ou modificado
		 */

		if (aAccess == null) {
			if (bAccess == null) {
				// no access, no change
				return false;
			} else {
				addChanged(ACCESS_ADDED, new Object[] { bAccess.image },
						aNode.getFirstToken(), bAccess, tipoElemento);
				return true;
			}
		} else if (bAccess == null) {
			addChanged(ACCESS_REMOVED, new Object[] { aAccess.image }, aAccess,
					bNode.getFirstToken(), tipoElemento);
			return true;
		} else if (aAccess.image.equals(bAccess.image)) {
			// no access change
			return false;
		} else {
			addChanged(ACCESS_CHANGED, new Object[] { aAccess.image,
					bAccess.image }, aAccess, bAccess, tipoElemento);
			return true;
		}
	}

	public void compareAccess(SimpleNode aNode, SimpleNode bNode,
			Relatorio.TipoDeclaracao tipoElemento) {
		Token aAccess = ItemUtil.getAccess(aNode);
		Token bAccess = ItemUtil.getAccess(bNode);

		/*
		 * Compara os modificadores de acesso pra verificar se foi adicionado ,
		 * removido ou modificado
		 */

		if (aAccess == null) {
			if (bAccess == null) {
				// no access, no change
			} else {
				
				addChanged(ACCESS_ADDED, new Object[] { bAccess.image },
						aNode.getFirstToken(), bAccess, tipoElemento);
			}
		} else if (bAccess == null) {

			addChanged(ACCESS_REMOVED, new Object[] { aAccess.image }, aAccess,
					bNode.getFirstToken(), tipoElemento);
		} else if (aAccess.image.equals(bAccess.image)) {
			// no access change
		} else {

			addChanged(ACCESS_CHANGED, new Object[] { aAccess.image,
					bAccess.image }, aAccess, bAccess, tipoElemento);
		}
	}

	protected void compareCode(String aName, List a, String bName, List b,
			Relatorio.TipoDeclaracao tipoElemento) {
		Diff d = new Diff(a, b, new DefaultComparator() {
			public int doCompare(Object x, Object y) {
				if (x instanceof Token) {
					if (y instanceof Token) {
						Token xt = (Token) x;
						Token yt = (Token) y;
						int cmp = xt.kind < yt.kind ? -1
								: (xt.kind > yt.kind ? 1 : 0);
						if (xt.kind == yt.kind) {
						}
						if (cmp == 0) {
							cmp = xt.image.compareTo(yt.image);
						}
						return cmp;
					} else {
						return 1;
					}
				} else if (x instanceof SimpleNode) {
					if (y instanceof SimpleNode) {
						SimpleNode xn = (SimpleNode) x;
						SimpleNode yn = (SimpleNode) y;
						int cmp = xn.getClass().getName()
								.compareTo(yn.getClass().getName());
						return cmp;
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			}
		});

		CodeReference ref = null;
		List diffList = d.diff();

		// tr.Ace.log("diffList", diffList);

		Iterator dit = diffList.iterator();

		Token lastA = null;
		Token lastB = null;

		while (dit.hasNext()) {
			Difference diff = (Difference) dit.next();

			int delStart = diff.getDeletedStart();
			int delEnd = diff.getDeletedEnd();
			int addStart = diff.getAddedStart();
			int addEnd = diff.getAddedEnd();

			// tr.Ace.log("diff", diff);

			String msg = null;
			Token aStart = null;
			Token aEnd = null;
			Token bStart = null;
			Token bEnd = null;

			if (delEnd == Difference.NONE) {
				if (addEnd == Difference.NONE) {
					// WTF?
					return;
				} else {
					aStart = getStart(a, delStart);
					aEnd = aStart;
					bStart = (Token) b.get(addStart);
					bEnd = (Token) b.get(addEnd);
					msg = CODE_ADDED;
				}
			} else if (addEnd == Difference.NONE) {
				aStart = (Token) a.get(delStart);
				aEnd = (Token) a.get(delEnd);
				bStart = getStart(b, addStart);
				bEnd = bStart;
				msg = CODE_REMOVED;
			} else {
				aStart = (Token) a.get(delStart);
				aEnd = (Token) a.get(delEnd);
				bStart = (Token) b.get(addStart);
				bEnd = (Token) b.get(addEnd);
				msg = CODE_CHANGED;
			}

			Point aStPt = CodeReference.toBeginPoint(aStart);
			Point aEndPt = CodeReference.toEndPoint(aEnd);
			Point bStPt = CodeReference.toBeginPoint(bStart);
			Point bEndPt = CodeReference.toEndPoint(bEnd);

			if (ref != null && ref.firstStart.x == aStPt.x) {
				ref.firstEnd = aEndPt;
				ref.secondEnd = bEndPt;
				ref.message = MessageFormat.format(CODE_CHANGED,
						new Object[] { aName });
				;
				ref.type = CodeReference.CHANGED;
			} else {
				String codeChgType = CodeReference.CHANGED;

				// the change type is add if the new line is on its own line:

				if (msg == CODE_ADDED
						&& onEntireLine(b, addStart, addEnd, bStart, bEnd)) {
					codeChgType = CodeReference.ADDED;
				} else if (msg == CODE_REMOVED
						&& onEntireLine(a, delStart, delEnd, aStart, aEnd)) {
					codeChgType = CodeReference.DELETED;
				}

				// This assumes that a and b have the same name. Wouldn't they?
				String str = MessageFormat.format(msg, new Object[] { aName });
				ref = new CodeReference(codeChgType, str, aStPt, aEndPt, bStPt,
						bEndPt, tipoElemento);
				add(ref);
			}
		}
	}

	protected boolean onEntireLine(List tokens, int tkIdxStart, int tkIdxEnd,
			Token startTk, Token endTk) {
		Token prevToken = tkIdxStart > 0 ? (Token) tokens.get(tkIdxStart - 1)
				: null;
		Token nextToken = tkIdxEnd + 1 < tokens.size() ? (Token) tokens
				.get(tkIdxEnd + 1) : null;

		boolean onEntLine = ((prevToken == null || prevToken.endLine < startTk.beginLine) && (nextToken == null || nextToken.beginLine > endTk.endLine));

		return onEntLine;
	}

	protected Token getStart(List list, int start) {
		Token stToken = start >= list.size() ? null : (Token) list.get(start);
		if (stToken == null && list.size() > 0) {
			stToken = (Token) list.get(list.size() - 1);
			stToken = stToken.next;
		}
		return stToken;
	}

	protected boolean compararCorpo(String aName, List a, String bName, List b,
			Relatorio.TipoDeclaracao tipoElemento) {
		Diff d = new Diff(a, b, new DefaultComparator() {
			public int doCompare(Object x, Object y) {
				if (x instanceof Token) {
					if (y instanceof Token) {
						Token xt = (Token) x;
						Token yt = (Token) y;
						int cmp = xt.kind < yt.kind ? -1
								: (xt.kind > yt.kind ? 1 : 0);
						if (xt.kind == yt.kind) {
						}
						if (cmp == 0) {
							cmp = xt.image.compareTo(yt.image);
						}
						return cmp;
					} else {
						return 1;
					}
				} else if (x instanceof SimpleNode) {
					if (y instanceof SimpleNode) {
						SimpleNode xn = (SimpleNode) x;
						SimpleNode yn = (SimpleNode) y;
						int cmp = xn.getClass().getName()
								.compareTo(yn.getClass().getName());
						return cmp;
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			}
		});

		CodeReference ref = null;
		List diffList = d.diff();

		// tr.Ace.log("diffList", diffList);

		Iterator dit = diffList.iterator();

		Token lastA = null;
		Token lastB = null;

		while (dit.hasNext()) {
			Difference diff = (Difference) dit.next();

			int delStart = diff.getDeletedStart();
			int delEnd = diff.getDeletedEnd();
			int addStart = diff.getAddedStart();
			int addEnd = diff.getAddedEnd();

			// tr.Ace.log("diff", diff);

			String msg = null;
			Token aStart = null;
			Token aEnd = null;
			Token bStart = null;
			Token bEnd = null;

			if (delEnd == Difference.NONE) {
				if (addEnd == Difference.NONE) {
					// WTF?
					return false;
				} else {
					aStart = getStart(a, delStart);
					aEnd = aStart;
					bStart = (Token) b.get(addStart);
					bEnd = (Token) b.get(addEnd);
					msg = CODE_ADDED;
					
					return true;
				}
			} else if (addEnd == Difference.NONE) {
				aStart = (Token) a.get(delStart);
				aEnd = (Token) a.get(delEnd);
				bStart = getStart(b, addStart);
				bEnd = bStart;
				msg = CODE_REMOVED;
				
				return true;
			} else {
				aStart = (Token) a.get(delStart);
				aEnd = (Token) a.get(delEnd);
				bStart = (Token) b.get(addStart);
				bEnd = (Token) b.get(addEnd);
				msg = CODE_CHANGED;
				
				return true;
			}
		}
		return false;
	}
}
