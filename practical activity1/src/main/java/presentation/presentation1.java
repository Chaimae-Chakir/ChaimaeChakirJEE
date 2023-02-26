package presentation;

import dao.DaoImpl;
import metier.MetierImpl;

public class presentation1 {
    public static void main(String[] args) {
        //injection of dependencies by static instantiation
        DaoImpl dao=new DaoImpl();
        MetierImpl metier=new MetierImpl();
        metier.setDao(dao);
        System.out.println("Date : "+metier.calcul());
    }
}
