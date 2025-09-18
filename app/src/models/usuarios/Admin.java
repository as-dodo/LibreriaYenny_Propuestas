package models.usuarios;

public class Admin extends Usuario {

    public Admin(String id, String nombre, String email, Rol rol) {
        super(id, nombre, email, rol);
    }

    @Override
    public String toString() {
        return "Admin{} " + super.toString();
    }
}
