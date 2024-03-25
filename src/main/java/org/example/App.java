package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        Configuration conf = new Configuration().configure();
        try(SessionFactory sF = conf.buildSessionFactory()){
            initTable(sF);
            showTable(sF);
            findStudentsOlder20(sF);
            removeStudent(sF,6);
            showTable(sF);

            newFirstName(3, "Igor", sF);
            showTable(sF);
            findStudentById(5, sF);
            findStudentById(55, sF);
        }
    }

    private static void findStudentById(int id, SessionFactory sF) {
        try(Session session = sF.openSession()){
            Transaction tx = session.beginTransaction();
            Student student = session.find(Student.class, id);
            if(student!=null){
                System.out.println(student);
                tx.commit();

            }else{
                System.out.println("Cannot find student with id: " + id);
            }
        }
    }

    private static void newFirstName(int id, String newName, SessionFactory sF) {
        try(Session session = sF.openSession()){
            Transaction tx = session.beginTransaction();
            Student student = session.find(Student.class, id);
            if(student!=null){
                System.out.println(student + " was update successfully!");
                student.setFirstName(newName);
                session.merge(student);
                tx.commit();

            }else{
                System.out.println("Cannot find student with id: " + id);
            }
        }
    }

    private static void removeStudent(SessionFactory sF, int id) {
        try(Session session = sF.openSession()){
            Transaction tx = session.beginTransaction();
            Student student = session.find(Student.class, id);
            if(student!=null){
                System.out.println(student + " was removed successfully!");
                session.remove(student);
                tx.commit();

            }else{
                System.out.println("Cannot find student with id: " + id);
            }
        }

    }

    private static void findStudentsOlder20(SessionFactory sF) {
        try(Session session = sF.openSession()){
            Transaction tx = session.beginTransaction();
            Query<Student> query = session.createQuery("select s from Student s where age > :age", Student.class);
            query.setParameter("age", 20);
            List<Student> result = query.getResultList();
            System.out.println("The list of students, who is older then 20 y.o: ");
            result.forEach(System.out::println);
            tx.commit();
        }
    }

    private static void showTable(SessionFactory sF) {
        try(Session session = sF.openSession()){
            Transaction tx = session.beginTransaction();
            Query<Student> query = session.createQuery("select s from Student s", Student.class);
            List<Student> result = query.getResultList();
            result.forEach(System.out::println);
            tx.commit();
        }
    }

    private static void initTable(SessionFactory sF) {
        try(Session session = sF.openSession()){
            Transaction tx = session.beginTransaction();
            Random rand = new Random();
            for (Long i = 1L; i < 11L; i++) {
                Student student = new Student(i, "firstName"+i, "secondName"+(i*2), (int) (16+i));
                session.persist(student);
            }
            tx.commit();
        }
    }
}
