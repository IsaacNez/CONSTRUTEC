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
    public class RoleController : ApiController
    {
        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<Role>> Get(string attribute, string id)
        {
            Role rol = null;
            List<Role> values = new List<Role>();
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            System.Diagnostics.Debug.WriteLine(ids[0]);
            System.Diagnostics.Debug.WriteLine("print1");
            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            myConnection.Open();
            string action = "";
            if (id != "undefined" )
            {
                var need = new UserController();
                action = need.FormConnectionString("dbrole", attr, ids);
            }
            else
            {
                action = "SELECT * FROM dbrole;";
            }
            System.Diagnostics.Debug.WriteLine("print4");
            System.Diagnostics.Debug.WriteLine(action);
            var command = new NpgsqlCommand(action, myConnection);
            System.Diagnostics.Debug.WriteLine("print5");
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            while (coso.Read())
            {
                rol = new Role();
                rol.r_id = (int)coso["r_id"];
                rol.r_name = (string)coso["r_name"];
                values.Add(rol);
            }

            myConnection.Close();
            return Json(values);

        }
    }
}
