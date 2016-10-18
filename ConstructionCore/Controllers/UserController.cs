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
            System.Diagnostics.Debug.WriteLine(us.u_lname);

            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test1 = "INSERT INTO EMPLOYEE(u_id,u_name,u_lname,u_phone,u_password, ) Values(:u_id,:u_name,:u_lname,:u_phone,:u_password)";
            string test = "insertuser";
            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.StoredProcedure;
            System.Diagnostics.Debug.WriteLine("print5");


            System.Diagnostics.Debug.WriteLine("generando comando");



            command.Parameters.AddWithValue(":m_id", us.u_id);

            command.Parameters.AddWithValue(":m_name", us.u_name);

            command.Parameters.AddWithValue(":m_lname", us.u_lname);

            command.Parameters.AddWithValue("m_phone", us.u_phone);

            command.Parameters.AddWithValue("m_password", us.u_password);
            command.Parameters.AddWithValue("m_code", us.u_code);
            command.Parameters.AddWithValue("rm_id", us.r_id);
            myConnection.Open();
            command.ExecuteNonQuery();

            System.Diagnostics.Debug.WriteLine("print6");


            // command.ExecuteReader();
            myConnection.Close();
        }

        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<ReturnedUser>> Get(string attribute, string id)
        {
            ReturnedUser us = null;
            List<ReturnedUser> values = new List<ReturnedUser>();
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            System.Diagnostics.Debug.WriteLine("print1");
            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "select * from getuser(" + Int32.Parse(ids[0]) + "," + "'"+ ids[1]+"');";
            System.Diagnostics.Debug.WriteLine(test);
            var command = new NpgsqlCommand(test, myConnection);
            myConnection.Open();
            var coso = command.ExecuteReader();
            us = new ReturnedUser();
            while (coso.Read()) {                
                us.q_id = (int)coso["q_id"];
                us.q_name = (string)coso["q_name"];
                System.Diagnostics.Debug.WriteLine((int)coso["q_code"]);
                us.q_code = (int)coso["q_code"];
                us.q_role.Add((int)coso["q_role"]);
            }
            values.Add(us);
            myConnection.Close();
            return Json(values);

        }
    }

    }

