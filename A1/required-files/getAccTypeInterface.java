import org.acplt.oncrpc.*;
import org.acplt.oncrpc.server.*;
import java.io.*;
import java.util.*;

public class getAccTypeInterface extends getAccTypeServerStub {

    private static String datafilename;
    private static String historyFileName;

    public getAccTypeInterface()
           throws OncRpcException, IOException {
        super();
    }

    public void run()
           throws OncRpcException, IOException {
     System.out.println("server unregister tports");
        unregister(transports);
     System.out.println("server register new tports");
        register(transports);
     System.out.println("run server tports");
        run(transports);
     System.out.println("server end run");
        unregister(transports);
        close(transports);
    }



   public  byte GET_ACC_TYPE_1(acc_id_num arg1){
    System.out.println("Processing request for "+ arg1.value );
	BufferedReader in=null;
	try{
		in = new BufferedReader(new FileReader(datafilename));
		//read file
		String line =null;
		while((line=in.readLine())!=null){
		     	StringTokenizer st = new StringTokenizer(line);
			// data file must have all 5 data fields
			if (st.countTokens()==3){
			// check the id to see if equal
				if(st.nextToken().equals(arg1.value)){
				return  (st.nextToken().getBytes())[0];
				}
			}
		}
		// if fall through then return error
		return 0;
	}
	catch (Exception e){
            System.out.println("error Processing request for "+ arg1.value );
	}
        return 0;
    }
    
   public int GET_BALANCE_1(acc_id_num arg1){
    System.out.println("Processing request for Get Balance for Account Id :"+ arg1.value );
	BufferedReader in=null;
	try{
		in = new BufferedReader(new FileReader(datafilename));
		//read file
		String line =null;
		while((line=in.readLine())!=null){
		     	StringTokenizer st = new StringTokenizer(line);
			if (st.countTokens()==3){
				if(st.nextToken().equals(arg1.value)){
                    String accountType= st.nextToken();
                    int bal = Integer.parseInt(st.nextToken());
				return  bal;
				}
			}
		}
		// if fall through then return error
		return -1;
	}
	catch (Exception e){
            System.out.println("Error Processing GET_BALANCE request for "+ arg1.value );
	}
        return -1;
    }

    //Operation 2 : add 'x' to the existing balance associated with arg1

    public BALANCE SET_BALANCE_1(acc_id_num arg1,int x)
    {
    System.out.println("Processing Set Balance Request for Account Id :"+ arg1.value );
    BALANCE balance = null;
	BufferedReader in=null;
    StringBuffer sb = new StringBuffer("");
    boolean flag =false;
	try{
		in = new BufferedReader(new FileReader(datafilename));
		//read file
		String line =null;
		while((line=in.readLine())!=null){
		     	StringTokenizer st = new StringTokenizer(line);
			// data file must have all 5 data fields
			if (st.countTokens()==3){
			// check the id to see if equal
				if(st.nextToken().equals(arg1.value)){
                    balance = new BALANCE();
                    String accountType= st.nextToken();
                    int bal = Integer.parseInt(st.nextToken());
                    balance.account = arg1;
                    balance.oldBalance = bal;
                    bal=bal+x;
                    balance.newBalance = bal;
                    sb.append(arg1.value+" "+accountType+" "+bal+"\n");
                    flag=true;
				}else
                {
                    sb.append(line);
                    sb.append("\n");
                }
			}
		}

        in.close();     
		// if fall through then return error
        BufferedWriter out = new BufferedWriter(new FileWriter(datafilename));
        out.write(sb.toString());
        out.flush();
        out.close();
        if(null==balance)
        {
            balance = new BALANCE();
            balance.account=arg1;
            balance.oldBalance = -100;// Returning Negative Amount if Account is not found.
        }
		return balance;
 
	}
	catch (Exception e){
            System.out.println("Error Processing SET Balance Request for Account Id :"+ arg1.value );
            balance = new BALANCE();
            balance.account=arg1;
            balance.oldBalance = -100;// Returning Negative Amount if Account is not found.
            return balance;
	}
    }


    public TRANS TRANSACTION_1(acc_id_num arg1,acc_id_num arg2,int x){
    System.out.println("Processing Transfer request from "+ arg1.value+" to " + arg2.value +" of Amount " + x );
	
    BufferedReader in=null;
    
    StringBuffer sb= new StringBuffer("");
	
    try{
		in = new BufferedReader(new FileReader(datafilename));
		
        String line =null;
        TRANS t= null;
        int oldBal1=-1,oldBal2=-1,newBal1,newBal2;
        String accType1="",accType2="";

        while((line=in.readLine())!=null)
        {
            String st[] = line.split(" ");
            
            if(st[0].equals(arg1.value) || st[0].equals(arg2.value))
            {

                if (st[0].equals(arg1.value))
                {
                    oldBal1 = Integer.parseInt(st[2]);
                    accType1 = st[1];
                }
                else if(st[0].equals(arg2.value))
                {
                    oldBal2 = Integer.parseInt(st[2]);
                    accType2 = st[1];
                }

                newBal1=oldBal1;
                newBal2=oldBal2;

                if(oldBal1 !=-1 && oldBal2 !=-1)
                {
                    if(oldBal1-x >= 0)
                    {
                    
                     newBal1= oldBal1 - x;
                     newBal2= oldBal2 + x;
                    
                    }
                    
                     BALANCE sender = new BALANCE();
                     BALANCE receiver =  new BALANCE();
			         
                     sender.account = arg1;
                     sender.newBalance = newBal1;
                     sender.oldBalance = oldBal1;
                    
                     receiver.account = arg2;
                     receiver.newBalance = newBal2;
                     receiver.oldBalance = oldBal2;

                     BALANCE[] balance = new BALANCE[2];
                     balance[0]= sender;
                     balance[1]= receiver;

                     t =  new TRANS(balance); 
                     //t.value[0]= sender;
                     //t.value[1]= receiver;
    
     
                     sb.append(arg1.value+" "+accType1+" "+newBal1+"\n");
                     sb.append(arg2.value+" "+accType2+" "+newBal2+"\n");
                }
            }else
            {
                sb.append(line);
                sb.append("\n");
            }

	    }
        
        in.close();    

        if(null==t)//Either of the Account data is not available
        {
           BALANCE dummy1 = new BALANCE();
           dummy1.account = arg1;
           dummy1.newBalance = -1;
           
           BALANCE dummy2 = new BALANCE();
           dummy2.account=arg2;
           dummy2.newBalance = -1;

           BALANCE[] dummy_bal = new BALANCE[2];
           dummy_bal[0]=dummy1;
           dummy_bal[1]=dummy2;
           t= new TRANS(dummy_bal);
           return t;
           
        } 
        BufferedWriter out = new BufferedWriter(new FileWriter(datafilename));
        out.write(sb.toString());
        out.flush();
        out.close();
        if(t.value[0].oldBalance!=t.value[0].newBalance)
        {
            logTransactionHistory(arg1.value, arg2.value, x);
        }
        
        return t;

    }
	catch (Exception e){
            System.out.println("Error Processing request for "+ arg1.value );
	}
        return null;
    }

    public void logTransactionHistory(String sender, String rec, int amount)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter("history.txt",true));
            bw.write(sender + " " + rec + " " +amount);
            bw.newLine();
            bw.flush();
            bw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

   public HISTORY GET_TRANSACTION_1(acc_id_num arg1){
    System.out.println("Processing request for "+ arg1.value );
	BufferedReader in=null;
	try{
		in = new BufferedReader(new FileReader(historyFileName));
		String line =null;
        HISTORY start = null;
        HISTORY end = null;
		while((line=in.readLine())!=null)
        {
		    String st[]= line.split(" "); 
            if(st[0].equals(arg1.value))
            {
                HISTORY data= new HISTORY();
                acc_id_num rec= new acc_id_num();
                rec.value= st[1];

                data.receiver = rec;
                data.transfer_amount=  Integer.parseInt(st[2]);
                data.next=null;
                if(start==null)
                {
                    start=data;
                    end=data;
                }else
                {
                    end.next=data;
                    end=data;    
                }
            }

		}
        in.close();
        if(null==start)
        {
            start= new HISTORY();
            start.transfer_amount=-100;
            start.receiver=arg1;
            start.next=null;
        }
        return start;
	}
	catch (Exception e){
            System.out.println("error Processing request for "+ arg1.value );
            HISTORY start = new HISTORY();
            start= new HISTORY();
            start.transfer_amount=-100;
            start.receiver=arg1;
            start.next=null;
            return start;
        }
    }

    public static void main(String [] args) {
        //check for file argument
	if (args.length >1) {
		System.out.println("usage: getAccTypeInterface [datafile]");
		System.exit(1);
	}
	if (args.length ==1) {
		datafilename=args[0];
	}
	else {
		datafilename="acc-new.txt";
	}

    historyFileName="history.txt";

	//test existance of datafile
	File f = new File(datafilename);
	if (!f.isFile()){
		// datafile is missing
		System.out.println(datafilename + " is not a valid file name \n Server aborting");
		System.exit(1);
	}

	try {
           System.out.println("Starting getAccTypeInterface...");
           getAccTypeInterface server = new getAccTypeInterface();
           server.run();
        } catch ( Exception e ) {
            System.out.println("Server error:");
            e.printStackTrace(System.out);
        }
        System.out.println("Server stopped.");
    }

}
