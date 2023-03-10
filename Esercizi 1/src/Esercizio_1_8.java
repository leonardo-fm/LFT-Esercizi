public class Esercizio_1_8 {

        public static void main(String[] args)
        {
            System.out.println(scan(args[0]) ? "OK" : "NOPE");
        }

        public static boolean scan(String s)
        {
            int state = 0;
            int i = 0;
            while (state >= 0 && i < s.length()) {
                final char ch = s.charAt(i++);
                switch (state) {
                    case 0:
                        if ("+-0123456789".contains(Character.toString(ch)) == true)
                            state = 1;
                        else if (".".contains(Character.toString(ch)) == true)
                            state = 2;
                        else
                            state = -1;
                        break;
                    case 1: // Final status
                        if (".".contains(Character.toString(ch)) == true)
                            state = 2;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 1;
                        else
                            state = -1;
                        break;
                    case 2:
                        if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 6;
                        else
                            state = -1;
                        break;
                    case 6: // Final state
                        if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 6;
                        else if (ch == 'e')
                            state = 3;
                        else
                            state = -1;
                        break;
                    case 3:
                        if ("+-0123456789".contains(Character.toString(ch)) == true)
                            state = 4;
                        else if (".".contains(Character.toString(ch)) == true)
                            state = 5;
                        else
                            state = -1;
                        break;
                    case 4: // Final status
                        if (".".contains(Character.toString(ch)) == true)
                            state = 5;
                        else if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 4;
                        else
                            state = -1;
                        break;
                    case 5:
                        if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 7;
                        else
                            state = -1;
                        break;
                    case 7: // Final state
                        if ("0123456789".contains(Character.toString(ch)) == true)
                            state = 7;
                        else
                            state = -1;
                        break;
                }
            }

            return state == 1 || state == 6 || state == 4 || state == 7;
        }
}
