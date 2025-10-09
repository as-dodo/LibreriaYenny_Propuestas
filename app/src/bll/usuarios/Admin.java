package bll.usuarios;

public class Admin extends Usuario {

    public Admin(String id, String nombre, String email, Rol rol) {
        super(id, nombre, email, rol);
    }

    public Admin(String id, String nombre, String email) {
        super(id, nombre, email, Rol.ADMIN);
    }

    @Override
    public String toString() {
        return "Admin{} " + super.toString();
    }
}
