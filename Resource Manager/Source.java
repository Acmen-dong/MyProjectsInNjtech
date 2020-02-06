package ��Դ������;

import java.io.*;
import javax.swing.*;//JTree��ʹ��
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.net.*;//������ȡ����IP
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Source extends JFrame implements MouseListener{
	JTree mainTree;							//��Դ������������
	DefaultTreeModel treeModel;				//����ģ�Ϳ�����ӵ�������
	DefaultMutableTreeNode root;			//���ĸ��ڵ�
	DefaultMutableTreeNode selectionNode;	//��ǰѡ�еĽڵ㣨���еĽڵ�λ�ã�
	
	Object [][] nodeList=new Object[0][0]; //�����洢�ڵ��list
	
	InetAddress localAddress; 			//���ص��Եĵ�ַ������ȡ���ص���Դ
	
	JPopupMenu popUp=new JPopupMenu();	//�������Ҽ����������Ӳ˵�
			
	String pathName;					//�������ƺ�·�������̵�·����
	String fileName;					//�������ļ���
	String copyFilename;				//�����ļ�������
	String copyPathname;				//�����ļ����ڵ�·��
	String goalPathname;				//Ŀ��Ĵ���·����
	
	
	
	
/**
 * @throws UnknownHostException
 * ���캯��
 */
Source() throws UnknownHostException {
		setPopup();
		this.setTitle("��Դ������");
		this.add(paneMake());
		this.setVisible(true);
		this.setPreferredSize(new Dimension(400,400));
		this.pack();
		setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}




/**
 * �Ҽ�����ʽ�˵��Ĺ���
 */
public void setPopup() {
	
	JMenuItem mu1=new JMenuItem("����");
	JMenuItem mu2=new JMenuItem("ճ��");
	JMenuItem mu3=new JMenuItem("ɾ��");
	popUp.add(mu1);
	popUp.add(mu2);
	popUp.add(mu3);
	mu1.addActionListener(new popActionlistener());
	mu2.addActionListener(new popActionlistener());
	mu3.addActionListener(new popActionlistener());
}



/**
 * @author ��
 *����ʽ�˵����ܵļ���
 */
class popActionlistener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="����") {		//�����ļ�����
			copyPathname=pathName;
			copyFilename=fileName;
			System.out.println("���Ƴɹ�");
		}
		//ճ���ļ�����
		if(e.getActionCommand()=="ճ��"&&(new File(pathName).isDirectory())) {
			goalPathname=pathName;
			goalPathname=goalPathname+"/"+copyFilename;
			System.out.println("���ļ�"+goalPathname);
			try {
				copyFile(copyPathname,goalPathname);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			DefaultMutableTreeNode node2=new DefaultMutableTreeNode(copyFilename);
			selectionNode.add(node2);
			SwingUtilities.invokeLater(new Runnable(){ 
				public void run(){
					mainTree.updateUI();
			}});
		}
		if(e.getActionCommand()=="ɾ��") {		//ɾ������
			File file=new File(pathName);
			file.delete();
			treeModel.removeNodeFromParent(selectionNode);

			System.out.println("�ѳɹ�ɾ��");
		}
	}
	
}
/**
 * @param soursefile
 * @param goalfile
 * @throws IOException
 * ����ʵ�ֱ����ļ��ĸ��ƺ�ճ��
 */
public void copyFile(String soursefile,String goalfile) throws IOException {
	File sourseFile=new File(soursefile);//Դ�ļ��Ļ��
	File goalFile=new File(goalfile);	//Ŀ���ļ��Ļ��
		FileInputStream fis = new FileInputStream(sourseFile);
		FileOutputStream fos = new FileOutputStream(goalFile);
		
		BufferedInputStream bis=new BufferedInputStream(fis);
		BufferedOutputStream bos=new BufferedOutputStream(fos);

		int b;
	      while((b=bis.read())!=-1){
	       bos.write(b);
	    }
	      bis.close();
	      bos.close();
}
	




/**
 * @return
 * @throws UnknownHostException
 * ���Ĺ���
 */
public JPanel paneMake() throws UnknownHostException {
	
	JPanel mainPane=new JPanel();				//������Ҫ���
	mainPane.setLayout(new BorderLayout());
	JScrollPane scrollPane = new JScrollPane(makeTreeStruct());
	scrollPane.setPreferredSize(new Dimension(200, 300));
	mainPane.add(scrollPane,BorderLayout.CENTER);
	return mainPane;
}



/**
 * @return
 * @throws UnknownHostException
 * ���ṹ�Ĺ��캯��
 */
public JTree makeTreeStruct() throws UnknownHostException{
	localAddress=InetAddress.getLocalHost();
	root=new DefaultMutableTreeNode(localAddress.getHostName());
	for(char i='c';i<'g';i++) {
		String path=i+":/";
		File file=new File(path);
		DefaultMutableTreeNode discnode=new DefaultMutableTreeNode(path);
		root.add(discnode);
		readFiles(discnode,file);
		
	}
	treeModel=new DefaultTreeModel(root);
	mainTree=new JTree(treeModel);
	mainTree.setEditable(true);
	mainTree.setShowsRootHandles(false);
	mainTree.collapseRow(0);
	
	//�������������
	mainTree.addTreeSelectionListener(new TreeSelectionListener() {
	
		@Override
		public void valueChanged(TreeSelectionEvent e) {//�Խڵ�ļ���
			// TODO Auto-generated method stub
			System.out.println("Node Valuechanged");
			
			selectionNode=(DefaultMutableTreeNode)(e.getPath().getLastPathComponent());
			TreeNode[] path=selectionNode.getPath();
			if(path==null)
				return;
			if(selectionNode.isRoot())
				return;
			
			pathName=path[1].toString();
			for(int i=2;i<path.length;i++)
				pathName=pathName+"/"+path[i].toString();
			fileName=path[path.length-1].toString();
			File file=new File(pathName);
			System.out.println("��ѡ�ļ��л��ļ�������"+fileName);
			SwingUtilities.invokeLater(new Runnable(){ 
				public void run(){
					mainTree.updateUI();
			}});
			selectionNode.removeAllChildren();
			readFiles(selectionNode,file);
			
			
		}
	});
	
	//չ�����ڵ�����������
	mainTree.addTreeExpansionListener(new TreeExpansionListener() {

		@Override
		public void treeCollapsed(TreeExpansionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		
		public void treeExpanded(TreeExpansionEvent e) {
			// TODO Auto-generated method stub
			
			System.out.println("expend");
		}
		
	});
	
	mainTree.addMouseListener(this);
	
	return mainTree;
}




/**
 * @param childNode
 * @param parent
 * ��ȡ�����ļ�
 */
public void readFiles(DefaultMutableTreeNode childNode,File parent)
{
	if(parent.isDirectory()) {				//������ڵ����ļ��еĻ�
	File [] list=parent.listFiles();
	for(int j=0;j<list.length;j++) {
		if(list[j].isDirectory()) {
			String nodename=list[j].getName();
			File nu=null;
			DefaultMutableTreeNode sbparent=new DefaultMutableTreeNode(nodename);
			DefaultMutableTreeNode nul=new DefaultMutableTreeNode(nu);
			sbparent.add(nul);
			childNode.add(sbparent);
		}
		else {
			String str=list[j].getName();
			DefaultMutableTreeNode child=new DefaultMutableTreeNode(str);
			childNode.add(child);
		}
	}
	}
}
//main����
public static void main(String[] args) throws Exception {
	new Source();
}

//�����Զ����ɼ�����������ֻ�õ��˵������
@Override
public void mouseClicked(MouseEvent e) {
	// TODO Auto-generated method stub
	TreePath path=mainTree.getPathForLocation(e.getX(), e.getY());
	//DefaultMutableTreeNode node=(DefaultMutableTreeNode)e.getSource();
	if(e.getButton()==MouseEvent.BUTTON3&&path!=null)
	{
		popUp.show(e.getComponent(), e.getX(), e.getY());
	}
}
@Override
public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub
}
@Override
public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub
}
@Override
public void mousePressed(MouseEvent arg0) {
	// TODO Auto-generated method stub
}
@Override
public void mouseReleased(MouseEvent arg0) {
	// TODO Auto-generated method stub
}
}

