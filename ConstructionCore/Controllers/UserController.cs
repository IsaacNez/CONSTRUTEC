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
                    ConnectionString = ConnectionString + attr[i] + " like '%" + ids[i] + "%';";
                    return ConnectionString;
                }
                else
                {
                    ConnectionString = ConnectionString + attr[i] + " like '%" + ids[i] + LogicalConnector + "%' ";
                }

            }
            return ConnectionString;
        }
        [HttpPost]
        [ActionName("Post")]
        public void AddUser(User us)
        {
            NpgsqlConnection myConnection = new NpgsqlConnection();
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            string test = "insertuser";
            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.StoredProcedure;
            command.Parameters.AddWithValue(":m_id", us.u_id);
            command.Parameters.AddWithValue(":m_name", us.u_name);
            command.Parameters.AddWithValue(":m_lname", us.u_lname);
            command.Parameters.AddWithValue("m_phone", us.u_phone);
            command.Parameters.AddWithValue("m_password", us.u_password);
            command.Parameters.AddWithValue("m_code", us.u_code);
            command.Parameters.AddWithValue("rm_id", us.r_id);
            myConnection.Open();
            command.ExecuteNonQuery();
            myConnection.Close();
        }

        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<RequestedUser>> Get(string attribute, string id)
        {
            RequestedUser us = null;
            List<RequestedUser> values = new List<RequestedUser>();
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            NpgsqlConnection myConnection = new NpgsqlConnection();
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("cargo base");
            myConnection.Open();
            string test = "select * from getuser("+ids[0]+",'"+ids[1]+"');";
            var command = new NpgsqlCommand(test, myConnection);
            var coso = command.ExecuteReader();
            us = new RequestedUser();
            while (coso.Read()) {             
                us.q_id = (int)coso["q_id"];                
                us.q_name = (string)coso["q_name"]; 
                us.q_code = (int)coso["q_code"];
                us.q_role.Add((int)coso["q_role"]);
                values.Add(us);
            }       
            myConnection.Close();
            return Json(values);

        }

    }
}
