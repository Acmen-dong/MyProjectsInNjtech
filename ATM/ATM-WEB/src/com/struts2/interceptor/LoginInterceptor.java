package com.struts2.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/*
 * ��������������һ���̳�MethodFilterInterceptor
 * �ڶ�������д�����doIntercept�ķ���
 * ������������interceptor��action�ķ���
 * 
 */
public class LoginInterceptor extends MethodFilterInterceptor{

	//�˷�������д�������߼�
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		HttpServletRequest request=ServletActionContext.getRequest();
		Object username=request.getSession().getAttribute("username");
		if(username!=null) {
			//��¼״̬��Ҫ��һ�����еĲ���
			System.out.println("����");
			invocation.invoke();
		}else {
			//���ǵ�¼״̬,��ִ�з��������ص�¼����
			//������ҳ����ȥ��login��result�� 
			System.out.println("ʧ�ܲ�����");
			return "login";
		}
		return null;
	}

}
