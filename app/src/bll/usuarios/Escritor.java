package bll.usuarios;

public class Escritor extends Usuario{

        private String biografia;

        public Escritor(String id, String nombre, String email, String biografia) {
            super(id, nombre, email, Rol.ESCRITOR);
            this.biografia = biografia;
        }

    public Escritor(String id, String nombre, String email) {
        super(id, nombre, email, Rol.ESCRITOR);
    }

        public String getBiografia() {
            return biografia;
        }

        public void setBiografia(String biografia) {
            this.biografia = biografia;
        }

    @Override
    public String toString() {
        return "Escritor{" +
                "biografia='" + biografia + '\'' +
                "} " + super.toString();
    }
}
