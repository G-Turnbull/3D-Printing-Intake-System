/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import domain.Account;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.AccountService;

/**
 *
 * @author harma
 */
public class AccountManagementController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        int accountID;
        AccountService as = new AccountService();
        
//        String action = request.getParameter("action");
//        if (action != null && action.equals("edit")) 
//        {
//            String accountSelected = request.getParameter("accountSelected");
//            accountID = Integer.parseInt(accountSelected);
//            try
//            {
//                Account account = as.getUser(accountID);
//                request.setAttribute("accountSelected", account);
//            } 
//            catch (Exception ex) 
//            {
//                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
        ArrayList<Account> accounts = as.getAllAccounts();
        
//        ArrayList<Account> accounts = null;
//        try  
//        {
//            
//        } 
//        catch (Exception ex) 
//        {
//            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
//        if(request.getParameter("techHome") !=null)
//        {
//            getServletContext().getRequestDispatcher("/WEB-INF/techHome.jsp").forward(request, response);
//            return;
//        }
        request.setAttribute("accounts", as.getAllAccounts());
        request.getRequestDispatcher("/WEB-INF/accountMgmt.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if(request.getParameter("testCreateAccount") != null)
        {
            AccountService as = new AccountService();
            if(as.createAccount("harmanjit.mohaar@edu.sait.ca", "password", "Harman", "Mohaar", "user") == 1)
            {
                request.setAttribute("message", "Successfully added trash to the DB");
                request.getRequestDispatcher("/WEB-INF/accountMgmt.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
        int accountID = 0;
        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        //String accountType = request.getParameter("accountType");

        AccountService as = new AccountService();
        request.setAttribute("accounts", as.getAllAccounts());
        if(action!=null)
        {
            try 
            {
                switch(action)
                {
                    case "add":
                        if(!(email == null || email.equals("")) && !(password == null || password.equals("")) 
                            && !(firstName == null || firstName.equals("")) && !(lastName == null || lastName.equals("")))
                        {                   
                            as.createAccount(email, password, firstName, lastName, "user");
                            request.setAttribute("addM", "New User added.");
                            getServletContext().getRequestDispatcher("/WEB-INF/accountMgmt.jsp").forward(request, response);   
                        }
                        else
                        {
                            request.setAttribute("errorM", "Please enter the required fields.");
                            getServletContext().getRequestDispatcher("/WEB-INF/accountMgmt.jsp").forward(request, response);
                        }
                        break;
                    case "edit":
                        if(!(email == null || email.equals("")) && !(password == null || password.equals("")) 
                            && !(firstName == null || firstName.equals("")) && !(lastName == null || lastName.equals("")))
                        {                   
                            String accID = request.getParameter("accountID");
                            accountID = Integer.parseInt(accID);
                            as.updateAccount(email, password, firstName, lastName, accountID, "user");
                            request.setAttribute("editM", "User has been updated.");
                            getServletContext().getRequestDispatcher("/WEB-INF/accountMgmt.jsp").forward(request, response);
                        }
                        else
                        {
                            request.setAttribute("errorM", "Please enter all of the required fields.");
                            getServletContext().getRequestDispatcher("/WEB-INF/accountMgmt.jsp").forward(request, response);
                        }

                        break;
                    case "delete":
                        String accID = request.getParameter("accountID");
                        accountID = Integer.parseInt(accID);
                        int acc = as.deleteAccount(accountID);
                        if (acc == 0)
                        {
                            request.setAttribute("errorDeleteM", "Can't delete this user.");
                            getServletContext().getRequestDispatcher("/WEB-INF/accountMgmt.jsp").forward(request, response);
                        }
                        else
                        {
                            as.deleteAccount(accountID);
                            request.setAttribute("deleteM", "User has been deleted.");
                            getServletContext().getRequestDispatcher("/WEB-INF/accountMgmt.jsp").forward(request, response); 
                            break;

                        }
                    default:
                        break;
                }
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
