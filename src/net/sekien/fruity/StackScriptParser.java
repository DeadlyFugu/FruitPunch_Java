package net.sekien.fruity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 12:34 PM To change this template use File | Settings |
 * File Templates.
 */
public class StackScriptParser {
	public static String[] parse(String in) {
		in = in.concat(" ");
		List<String> tokens = new ArrayList<String>(128);
		StringBuilder token = new StringBuilder(128);
		char p = ' ';
		boolean csyn = false, str = false, cm = false, linecm = false;
		for (char c : in.toCharArray()) {
			if (cm) {
				if (c == '\n') {
					if (linecm) cm = false;
				} else if (c == '/') {
					if (p == '*' && !linecm) cm = false;
				}
				p = c;
			} else if (p != '\\') switch (c) {
				case '\n':
				case ' ':
				case '\t':
					if (str || csyn) {token.append(c); break;}
					if (token.length() != 0) {
						tokens.add(token.toString());
						token.setLength(0);
					} break;
				case '\\':
					if (str || csyn) {break;} else throw new SException("SYNTAX: WHY IS \\ HERE?");
				case '[':
					if (str || csyn) {token.append(c); break;}
					csyn = true;
					token.append(c);
					tokens.add(token.toString());
					token.setLength(0);
					break;
				case '(':
				case '{':
					if (str || csyn) {token.append(c); break;}
					token.append(c);
					tokens.add(token.toString());
					token.setLength(0);
					break;
				case ']':
					if (str) {token.append(c); break;}
					csyn = false;
					tokens.add(token.toString());
					token.setLength(0);
					break;
				case ')':
				case '}':
					if (str || csyn) {token.append(c); break;}
					if (token.length() != 0) {
						tokens.add(token.toString());
						token.setLength(0);
					}
					token.append(c);
					tokens.add(token.toString());
					token.setLength(0);
					break;
				case '\"':
					if (str) {
						str = false;
						tokens.add(token.toString());
						token.setLength(0);
					} else if (!csyn) {
						if (token.length() != 0) {
							tokens.add(token.toString());
							token.setLength(0);
							throw new SException("WARN: Mid-line quote");
						}
						str = true;
						token.append(c);
					} else {
						token.append(c);
					}
					break;
				case '/': {
					if (!csyn && !str && p == '/') {
						token.deleteCharAt(token.length()-1);
						cm = true; linecm = true;
					} else {
						token.append(c);
					}
				} break;
				case '*': {
					if (!csyn && !str && p == '/') {
						token.deleteCharAt(token.length()-1);
						cm = true; linecm = false;
					} else {
						token.append(c);
					}
				} break;
				default:
					token.append(c);
			}
			else if (str || csyn) switch (c) {
				case '0': token.append('\0'); break;
				case 'n': token.append('\n'); break;
				case 't': token.append('\t'); break;
				case '"': token.append('\"'); break;
				case '\'': token.append('\''); break;
				case ')': token.append(']'); break;
			}
			else {
				throw new SException("SYNTAX: WHY IS \\ HERE?");
			}
			p = c;
		}
		if (str || csyn) {
			throw new SException("WARN: unclosed string or macro");
		}
		String[] ret = new String[tokens.size()];
		return tokens.toArray(ret);
	}
}
