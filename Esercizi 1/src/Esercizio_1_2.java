public class Esercizio_1_2 {

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
                        if ("0123456789".contains(Character.toString(ch)) == true)
                            state = -1;
                        else if (ch == '_')
                            state = 1;
                        else if ("abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true)
                            state = 2;
                        else
                            state = -1;
                        break;
                    case 1:
                        if (ch == '_')
                            state = 1;
                        else if ("0123456789".contains(Character.toString(ch)) == true
                            || "abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true)
                            state = 2;
                        else
                            state = -1;
                        break;
                    case 2:
                        if ("0123456789".contains(Character.toString(ch)) == true
                            || "abcdefghijklmnopqrstuvwxyz".contains(Character.toString(ch)) == true
                            || ch == '_')
                            state = 2;
                        else
                            state = -1;
                        break;
                }
            }

            return state == 2;
        }
}
