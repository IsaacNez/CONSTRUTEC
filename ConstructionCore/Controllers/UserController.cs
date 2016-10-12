using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Npgsql;
using ConstructionCore.Models;
using System.Data;
using System.Text;
using System.Web.Http.Results;
using System.Web.Http.Cors;

namespace ConstructionCore.Controllers
{
    [EnableCors(origins: "*", headers: "*", methods: "*")]
    public class UserController : ApiController
    {

        public string FormConnectionString(string baseString, string[] attr, string[] ids)
        {
            string ConnectionString = "SELECT * FROM " + baseString + " WHERE ";
            string LogicalConnector = "OR";
            if (ids == null)
            {
                return "SELECT * FROM " + baseString + ";";
            }
            if (attr.Length > 1)
            {
                LogicalConnector = "AND";
            }
            System.Diagnostics.Debug.WriteLine(attr.Length);
            for (int i = 0; i < attr.Length; i++)
            {
                System.Diagnostics.Debug.WriteLine(i);
                if (i == (attr.Length - 1))
                {
                    ConnectionString = ConnectionString + attr[i] + "=" + ids[i] + ";";
                    return ConnectionString;
                }
                else
                {
                    ConnectionString = ConnectionString + attr[i] + "=" + ids[i] + LogicalConnector + " ";
                }

            }
            return ConnectionString;
        }
        [HttpPost]
        [ActionName("Post")]
        public void AddUser(User us)
        {
            System.Diagnostics.Debug.WriteLine("print1");

            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "INSERT INTO EMPLOYEE(u_id,u_name,u_lname,u_phone,u_password, ) Values(@u_id,@u_name,@u_lname,@u_phone,@u_password)";
            var command = new NpgsqlCommand(test, myConnection);
            System.Diagnostics.Debug.WriteLine("print5");


            System.Diagnostics.Debug.WriteLine("generando comando");


            var uID = command.CreateParameter();
            uID.ParameterName = "u_id";
            uID.Value = us.u_id;
            command.Parameters.Add(uID);
            var uNa = command.CreateParameter();
            uNa.ParameterName = "u_name";
            uNa.Value = us.u_lname;
            command.Parameters.Add(uNa);
            var uLn = command.CreateParameter();
            uLn.ParameterName = "u_lname";
            uLn.Value = us.u_lname;
            command.Parameters.Add(uLn);
            var uPh = command.CreateParameter();
            uPh.ParameterName = "u_phone";
            uPh.Value = us.u_phone;
            command.Parameters.Add(uPh);
            var uPass = command.CreateParameter();
            uPass.ParameterName = "u_password";
            uPass.Value = us.u_password;
            command.Parameters.Add(uPass);

            System.Diagnostics.Debug.WriteLine("print6");
            myConnection.Open();
            int rowInserted = command.ExecuteNonQuery();
            myConnection.Close();
        }

        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<User>> Get(string attribute, string id)
        {
            User us = null;
            List<User> values = new List<User>();
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            System.Diagnostics.Debug.WriteLine("print1");
            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            myConnection.Open();
            string action = "";
            if (id != "undefined")
            {
               
                action = FormConnectionString("dbuser", attr, ids);
            }
            else
            {
                action = "SELECT * FROM dbuser;";
            }
            System.Diagnostics.Debug.WriteLine("print4");
            System.Diagnostics.Debug.WriteLine(action);
            var command = new NpgsqlCommand(action, myConnection);
            System.Diagnostics.Debug.WriteLine("print5");
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            while (coso.Read()) {
                us = new User();
                us.u_id = (int)coso["u_id"];
                us.u_charge = (string)coso["u_charge"];
                us.u_lname = (string)coso["u_charge"];
                us.u_name = (string)coso["u_name"];
                us.u_password = (string)coso["u_password"];
                us.u_phone = (int)coso["u_phone"];
                values.Add(us);
            }
       
            myConnection.Close();
            return Json(values);

        }

    }
}
