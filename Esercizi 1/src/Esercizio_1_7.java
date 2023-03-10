public class Esercizio_1_7 {

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
                        if (ch == 'L')
                            state = 1;
                        else
                            state = 3;
                        break;
                    case 1:
                        if (ch == 'e')
                            state = 2;
                        else
                            state = 4;
                        break;
                    case 2:
                        if (ch == 'o')
                            state = 5;
                        else
                            state = 5;
                        break;
                    case 3:
                        if (ch == 'e')
                            state = 4;
                        else
                            state = -1;
                        break;
                    case 4:
                        if (ch == 'o')
                            state = 5;
                        else
                            state = -1;
                        break;
                    case 5: // Final state
                        state = -1;
                        break;
                }
            }

            return state == 5;
        }
}
