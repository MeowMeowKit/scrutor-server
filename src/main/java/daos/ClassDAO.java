package daos;

import java.util.ArrayList;

public class ClassDAO {
    // Get all the classes that are taught by teacher A
    public static ArrayList<Class> getClassesByTeacherId(String teacherId) {
        return null;
    }

    // Get all the classes that student A attends
    public static ArrayList<Class> getClassesByStudentId(String studentId) {
        return null;
    }

    // Create a new class that taught by teacher A
    public static Class createNewClass(Class c, String teacherId){
        return null;
    }

    // A student attends a new class
    public static boolean attendNewClass(Class c, String studentId) {
        return false;
    }

    // Update class (edit className; edit student list add, remove student from class)
    public static boolean updateClass() {
        return false;
    }

    // Delete a class
    public static boolean deleteClass() {
        return false;
    }
}
