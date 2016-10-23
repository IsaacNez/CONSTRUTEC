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
                com.c_id = (int)coso["c_id"];
                com.s_name = (string)coso["s_name"];
                
                values.Add(com);
            }

            myConnection.Close();
            return Json(values);

        }
    }
}
