package com.codigo.wl.questao2.shared.entidade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @Date 29/07/2017 @Time 12:14:45
 * @author Wellington Lins Claudino Duarte
 * @mail wellingtonlins2013@gmail.com
 */
public class Salvando extends UnicastRemoteObject implements Runnable {

    private Thread thread;
    private String threadName;
    private EntityManagerFactory emf;
    private List<User> userList;
    private long inicio;
    private long fim;
    private long x;

    public Salvando() throws RemoteException {
    }

    public Salvando(String name, EntityManagerFactory emf, List<User> userList) throws RemoteException {

        threadName = name;
        this.emf = emf;
        this.userList = userList;
        System.out.println("Creating " + threadName);

    }

    @Override
    public void run() {
    
        System.out.println("Running " + threadName);
        try {
            EntityManager em1 = emf.createEntityManager();
            inicio = System.currentTimeMillis();
            for (User user : userList) {
                try {
                    em1.getTransaction().begin();
                    em1.persist(user);
                    em1.getTransaction().commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    em1.getTransaction().rollback();
                }
            }
            fim = System.currentTimeMillis();
            x = fim - inicio;

            System.out.println("\nTEMPO DECORRIDO PARA O  " + threadName + " " + x + "\n");

            em1.close();
            emf.close();
        } catch (Exception e) {
            System.out.println("Thread " + threadName + " interrupted.");
        }
        System.out.println("Thread " + threadName + " exiting.");
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}
