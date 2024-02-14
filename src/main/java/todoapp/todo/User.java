package todoapp.todo;


    public class User {
        private static String username;

        private static int u_id;

//



        public void setUsername(String username) {
            this.username = username;
        }
        public void setU_id(int u_id){
            this.u_id=u_id;
        }
//        public int getU_id(){
//            return u_id;
//        }
        public static int getId(){
            return u_id;
        }
        public static String getUsername(){
            return username ;
        }
    }

