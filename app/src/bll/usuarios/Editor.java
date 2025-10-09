package bll.usuarios;

public class Editor extends Usuario{

    private String especialidad;

    public Editor(String id, String nombre, String email, String especialidad) {
        super(id, nombre, email, Rol.EDITOR);
        this.especialidad = especialidad;
    }

    public Editor(String id, String nombre, String email) {
        super(id, nombre, email, Rol.EDITOR);
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return "Editor{" +
                "especialidad='" + especialidad + '\'' +
                "} " + super.toString();
    }
}
