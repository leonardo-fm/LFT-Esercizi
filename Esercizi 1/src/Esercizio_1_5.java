public class Esercizio_1_5 {

        public static void main(String[] args) { System.out.println(scan(args[0]) ? "OK" : "NOPE"); }

        public static boolean scan(String s)
        {
            int state = 0;
            int i = 0;
            while (state >= 0 && i < s.length()) {
                final char ch = s.charAt(i++);
                switch (state) {
                    case 0: // Start
                        if (ch == ' ')
                            state = 0;
                        else if ("ABCDEFGHIJK".contains(Character.toString(ch)) == true)
                            state = 1;
                        else if ("LMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true)
                            state = 8;
                        else
                            state = -1;
                        break;
                    case 1: // A-K Surnames first lower letter
                        if ("abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true)
                            state = 2;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 4;
                        else
                            state = -1;
                        break;
                    case 2: // A-K Surnames lower letters
                        if (ch == ' ')
                            state = 3;
                        else if ("abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true)
                            state = 2;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 4;
                        else
                            state = -1;
                        break;
                    case 3: // A-K space surname
                        if (ch == ' ')
                            state = 3;
                        else if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true)
                            state = 1;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 4;
                        else
                            state = -1;
                        break;
                    case 4: // Number selection
                        if ("02468".contains(Character.toString(ch)) == true)
                            state = 5;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 6;
                        else
                            state = -1;
                        break;
                    case 5: // Even numbers A-K | Final state
                        if (ch == ' ')
                            state = 7;
                        else if ("02468".contains(Character.toString(ch)) == true)
                            state = 5;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 6;
                        else
                            state = -1;
                        break;
                    case 6: // Odd numbers A-K
                        if ("02468".contains(Character.toString(ch)) == true)
                            state = 5;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 6;
                        else
                            state = -1;
                        break;
                    case 7: // Final state
                        if (ch == ' ')
                            state = 7;
                        else
                            state = -1;
                        break;

                        /* ========================================================== */

                    case 8: // L-Z Surnames first lower letter
                        if ("abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true)
                            state = 9;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 11;
                        else
                            state = -1;
                        break;
                    case 9: // L-Z Surnames lower letters
                        if (ch == ' ')
                            state = 10;
                        else if ("abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true)
                            state = 9;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 11;
                        else
                            state = -1;
                        break;
                    case 10: // L-Z space surname
                        if (ch == ' ')
                            state = 10;
                        else if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true)
                            state = 8;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 11;
                        else
                            state = -1;
                        break;
                    case 11: // Number selection
                        if ("02468".contains(Character.toString(ch)) == true)
                            state = 12;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 13;
                        else
                            state = -1;
                        break;
                    case 12: // Even numbers L-Z
                        if ("02468".contains(Character.toString(ch)) == true)
                            state = 12;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 13;
                        else
                            state = -1;
                        break;
                    case 13: // Odd numbers A-K | Final state
                        if (ch == ' ')
                            state = 14;
                        else if ("02468".contains(Character.toString(ch)) == true)
                            state = 12;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 13;
                        else
                            state = -1;
                        break;
                    case 14: // Final state
                        if (ch == ' ')
                            state = 14;
                        else
                            state = -1;
                        break;
                }
            }

            return state == 5 || state == 7 || state == 13 || state == 14;
        }
}
