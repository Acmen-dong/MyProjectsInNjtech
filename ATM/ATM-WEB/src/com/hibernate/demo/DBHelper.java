package com.hibernate.demo;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class DBHelper {
	
	//�û�ע��
	public void RegistUser(String username, String password, int status, int balance, String name, String phone, String address) {
        // ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        //����һ����user����
        User u = new User();
        Info info = new Info();
        u.setUsername(username);
        u.setPassword(password);
        u.setStatus(status);
        info.setAddress(address);
        info.setBalance(balance);
        info.setName(name);
        info.setPhone(phone);
        //����
        session.save(u);
        session.save(info);
        
        //�����ύ
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();   
	}
	
	//�û�ɾ��
	public void deleteUser(int id) {
		Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        User u = session.get(User.class, id);
        Info i = session.get(Info.class, id);
        session.delete(u);
        session.delete(i);
        
        //�����ύ
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
	}
	
	//�޸��û���Ϣ
	public void updapteUserName(int id, String newName) {
		Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        Info i = session.get(Info.class, id);
        i.setName(newName);
        session.update(i);
        
        //�����ύ
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
	}
	
	public void updapteUserPhone(int id, String newPhone) {
		Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        Info i = session.get(Info.class, id);
        i.setPhone(newPhone);
        session.update(i);
        
        //�����ύ
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
	}
	
	public void updapteUserAddress(int id, String newAddress) {
		Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        Info i = session.get(Info.class, id);
        i.setAddress(newAddress);
        session.update(i);
        
        //�����ύ
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
	}
	
	//��ȡ��
	//���
	public void deposit(int id, int money, boolean isTrans) {
		Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        Info i = session.get(Info.class, id);
        i.setBalance( i.getBalance() + money );
        session.update(i);
        
        if(!isTrans) {
        	Record r = new Record();
            r.setAccountId(id);
            r.setMoney(money);
		    SimpleDateFormat TimeFormatter = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy/MM/dd");
		    Date date = new Date(System.currentTimeMillis());
			r.setTime(TimeFormatter.format(date));
			r.setDate(DateFormatter.format(date));
			r.setStatus(1);
			System.out.println(r.getDate()+r.getTime());
			session.save(r);
        }
        
        //�����ύ
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
	}
	
	public boolean withdraw(int id, int money, boolean isTrans) {
		Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        Info i = session.get(Info.class, id);
        if(i.getBalance() < money) {
        	//�������
            session.close();
            sessionFactory.close();
            
            return false;
        }
        else{
        	i.setBalance( i.getBalance() - money );
        	session.update(i);
        
        	if(!isTrans) {
            	Record r = new Record();
                r.setAccountId(id);
                r.setMoney(money);
    		    SimpleDateFormat TimeFormatter = new SimpleDateFormat("HH:mm:ss");
    			SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy/MM/dd");
    		    Date date = new Date(System.currentTimeMillis());
    			r.setTime(TimeFormatter.format(date));
    			r.setDate(DateFormatter.format(date));
    			r.setStatus(-1);
    			session.save(r);
            }
        	
        	//�����ύ
        	session.getTransaction().commit();
        	session.close();
        	sessionFactory.close();
        	
        	return true;
        }
	}
	
	//ת��
	public boolean transfer(int userId, int targetId, int money) {
		Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        boolean isMoneyEnough = withdraw(userId, money, true);
        if(!isMoneyEnough) {
        	return false;
        } else {
        	deposit(targetId, money, true);
        	
        	Record userRecord = new Record();
        	userRecord.setAccountId(userId);
        	userRecord.setMoney(money);
		    SimpleDateFormat TimeFormatter = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy/MM/dd");
		    Date date = new Date(System.currentTimeMillis());
		    userRecord.setTime(TimeFormatter.format(date));
		    userRecord.setDate(DateFormatter.format(date));
		    userRecord.setStatus(-2);
			session.save(userRecord);
			
			Record targetRecord = new Record();
			targetRecord.setAccountId(targetId);
			targetRecord.setMoney(money);
			targetRecord.setTime(TimeFormatter.format(date));
			targetRecord.setDate(DateFormatter.format(date));
			targetRecord.setStatus(2);
			session.save(targetRecord);
			
			//�����ύ
        	session.getTransaction().commit();
        	session.close();
        	sessionFactory.close();
			
			return true;
        }
	}
	
	//��½
	public int GetLoginStatus(String username, String password) {
		// ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        Query q = session.createQuery("from User where username=\'" + username + "\' and password=\'" + password +"\'");
        List<User> list= q.list();
        
        if(list.isEmpty()) {
        	session.close();
            sessionFactory.close();
        	return -1;
        }
        else {
        	User u = list.get(0);
        	System.out.println(u.getStatus());
        	session.close();
            sessionFactory.close();
        	return u.getStatus();
        }
	}
	
	//��ȡ��¼�û�ID
	public int GetLoginId(String username, String password) {
		// ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        Query q = session.createQuery("from User where username=\'" + username + "\' and password=\'" + password +"\'");
        List<User> list= q.list();
        
        User u = list.get(0);
        
        session.close();
        sessionFactory.close();
        
        return u.getAccountId();
	}
	
	//��ѯ�û���Ϣ
	public Info getUserInfo(int id) {
		Info i = new Info();
		// ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        i = session.get(Info.class, id);
        
        // �����ύ
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
        
		return i;
	}
	
	//�޸�����
	public boolean changePassword(int id, String oldPassword, String newPassword) {
		// ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        User u = session.get(User.class, id);
        if(oldPassword.equals(u.getPassword())) {
        	u.setPassword(newPassword);
            session.update(u);
            
            //�����ύ
            session.getTransaction().commit();
            session.close();
            sessionFactory.close();
            
            return true;
        } else {
        	//�����ύ
        	System.out.println("���������");
            session.close();
            sessionFactory.close();
            
        	return false;
        }
        
        
      
	}
	
	//��ѯ��¼
	public List<Record> getRecord(int id) {
		// ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
		Query q = session.createQuery("from Record where AccountId=" + id);
        List<Record> list= q.list();
        
        session.close();
        sessionFactory.close();
        
        return list;
	}
	
	public int getTodayWithdraw(int userid) {
		// ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy/MM/dd");
	    Date date = new Date(System.currentTimeMillis());
	    String todayDate = DateFormatter.format(date);
		
        Query q = session.createQuery("from Record where AccountId=" + userid + " and Date=\'" + todayDate + "\' and status = -1");
        
        int result = 0;
        
        List<Record> list = q.list();
        for(int i=0; i<list.size(); i++) {
        	result += list.get(i).getMoney();
        }
        
        System.out.println("����ȡ���" + result);
        
        session.close();
        sessionFactory.close();
        
        return result;
	}
	
	public List<Info> getUserInfo(){
		// ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        Query q = session.createQuery("from Info where AccountId != 1");
        List<Info> list = q.list();
        
        session.close();
        sessionFactory.close();
        return list;
	}
	
	public String getUsername(int id) {
		// ʹ��Hibernate��API����ɽ�Customer��Ϣ���浽mysql���ݿ��еĲ���
        Configuration config = new Configuration().configure(); // Hibernate��ܼ���hibernate.cfg.xml�ļ�
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession(); // �൱�ڵõ�һ��Connection
        // ��������
        session.beginTransaction();
        
        User user = session.get(User.class, id);
        
        session.close();
        sessionFactory.close();
        
        return user.getUsername();
	}
}