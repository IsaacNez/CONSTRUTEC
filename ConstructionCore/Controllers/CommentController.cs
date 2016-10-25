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
    public class CommentController : ApiController
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
                LogicalConnector = " AND";
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
        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<Comment>> Get(string attribute, string id)
        {
            Comment com = null;
            List<Comment> values = new List<Comment>();
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
                var need = new ProjectController();
                action = need.FormConnectionString("dbcomment", attr, ids);
            }
            else
            {
                action = "SELECT * FROM dbproduct;";
            }
            System.Diagnostics.Debug.WriteLine("print4");
            System.Diagnostics.Debug.WriteLine(action);
            var command = new NpgsqlCommand(action, myConnection);
            System.Diagnostics.Debug.WriteLine("print5");
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            while (coso.Read())
            {
                com = new Comment();
                com.c_description = (string)coso["c_description"];
                com.s_name = (string)coso["s_name"];
                
                values.Add(com);
            }

            myConnection.Close();
            return Json(values);

        }
        [HttpPost]
        [ActionName("Post")]
        public void AddStage(Comment comment)
        {
            System.Diagnostics.Debug.WriteLine("print1");
            System.Diagnostics.Debug.WriteLine(comment.s_name);

            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "INSERT INTO dbcomment(c_description,s_name, p_id, u_id) Values(:c_description,:s_name,:p_id, :u_id)";

            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.Text;
            System.Diagnostics.Debug.WriteLine("print5");


            System.Diagnostics.Debug.WriteLine("generando comando");

            command.Parameters.AddWithValue(":c_description", comment.c_description);
            command.Parameters.AddWithValue(":s_name", comment.s_name);
            command.Parameters.AddWithValue("p_id", comment.p_id);
            command.Parameters.AddWithValue("u_id", comment.u_id);
            myConnection.Open();
            command.ExecuteNonQuery();

            System.Diagnostics.Debug.WriteLine("print6");

            myConnection.Close();

        }
    }
}
