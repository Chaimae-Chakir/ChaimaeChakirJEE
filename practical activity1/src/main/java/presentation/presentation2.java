package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class presentation2 {
    public static void main(String[] args) throws Exception {
        //injection of dependencies by dynamic instantiation
        Scanner scanner=new Scanner(new File("config.txt"));
        String classNameDao=scanner.nextLine();
        Class cDao=Class.forName(classNameDao);
        IDao dao=(IDao) cDao.newInstance();

        String classNameMetier=scanner.nextLine();
        Class cMetier=Class.forName(classNameMetier);
        IMetier metier=(IMetier) cMetier.newInstance();

        Method method=cMetier.getMethod("setDao",IDao.class);
        method.invoke(metier,dao);
        System.out.println("Date : "+metier.calcul());
    }
}
