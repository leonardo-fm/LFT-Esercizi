public class Esercizio_1_4 {

        public static void main(String[] args) { System.out.println(scan(args[0]) ? "OK" : "NOPE"); }

        public static boolean scan(String s)
        {
            int state = 0;
            int i = 0;
            while (state >= 0 && i < s.length()) {
                final char ch = s.charAt(i++);
                switch (state) {
                    case 0:
                        if (ch == ' ')
                            state = 0;
                        else if ("02468".contains(Character.toString(ch)) == true)
                            state = 1;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 2;
                        else if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true)
                            state = -1;
                        else
                            state = -1;
                        break;
                    case 1: // Even numbers
                        if ("02468".contains(Character.toString(ch)) == true)
                            state = 1;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 2;
                        else if (ch == ' ')
                            state = 3;
                        else if ("ABCDEFGHIJK".contains(Character.toString(ch)) == true)
                            state = 5;
                        else if ("LMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true) // Shift 4
                            state = -1;
                        else
                            state = -1;
                        break;
                    case 2: // Odd numbers
                        if ("02468".contains(Character.toString(ch)) == true)
                            state = 1;
                        else if ("13579".contains(Character.toString(ch)) == true)
                            state = 2;
                        else if (ch == ' ')
                            state = 4;
                        else if ("ABCDEFGHIJK".contains(Character.toString(ch)) == true) // Shift 1
                            state = -1;
                        else if ("LMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true)
                            state = 5;
                        else
                            state = -1;
                        break;
                    case 3: // After a space even num
                        if (ch == ' ')
                            state = 3;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = -1;
                        else if ("ABCDEFGHIJK".contains(Character.toString(ch)) == true)
                            state = 5;
                        else if ("LMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true) // Shift 4
                            state = -1;
                        else
                            state = -1;
                        break;
                    case 4: // After a space odd num
                        if (ch == ' ')
                            state = 4;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = -1;
                        else if ("ABCDEFGHIJK".contains(Character.toString(ch)) == true) // Shift 1
                            state = -1;
                        else if ("LMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true)
                            state = 5;
                        else
                            state = -1;
                        break;
                    case 5:
                        if ("0123456789".contains(Character.toString(ch)) == true)
                            state = -1;
                        else if (ch == ' ')
                            state = -1;
                        else if ("abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true)
                            state = 6;
                        else
                            state = -1;
                        break;
                    case 6: // End state with no spaces
                        if ("0123456789".contains(Character.toString(ch)) == true)
                            state = -1;
                        else if (ch == ' ')
                            state = 7;
                        else if ("abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true)
                            state = 6;
                        else
                            state = -1;
                        break;
                    case 7: // End state with spaces
                        if ("0123456789".contains(Character.toString(ch)) == true)
                            state = -1;
                        else if (ch == ' ')
                            state = 7;
                        else if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(Character.toString(ch)) == true)
                            state = 5;
                        else
                            state = -1;
                        break;
                }
            }

            return state == 6 || state == 7;
        }
}
