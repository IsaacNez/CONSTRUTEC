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
    public class StageController : ApiController
    {
        [HttpPost]
        [ActionName("Post")]
        public void AddStage(Stage stage)
        {
            System.Diagnostics.Debug.WriteLine("print1");
             System.Diagnostics.Debug.WriteLine(stage.s_name);

             NpgsqlConnection myConnection = new NpgsqlConnection();
             System.Diagnostics.Debug.WriteLine("print2");
             myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
             System.Diagnostics.Debug.WriteLine("print3");
             System.Diagnostics.Debug.WriteLine("cargo base");
             string test = "INSERT INTO stage(s_name,s_description) Values(:s_name,:s_description)";

             var command = new NpgsqlCommand(test, myConnection);
             command.CommandType = CommandType.Text;
             System.Diagnostics.Debug.WriteLine("print5");


             System.Diagnostics.Debug.WriteLine("generando comando");



             command.Parameters.AddWithValue(":s_name", stage.s_name);
             command.Parameters.AddWithValue(":s_description", stage.s_description);
             
             myConnection.Open();
             command.ExecuteNonQuery();
             System.Diagnostics.Debug.WriteLine("print6");
             myConnection.Close();

         
        }
        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<Stage>> Get(string attribute, string id)
        {
            Stage stag = null;
            List<Stage> values = new List<Stage>();
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
            if (id != "undefined"&&ids[0]!="stages")
            {
                var need = new UserController();
                action = need.FormConnectionString("stage", attr, ids);
            }
            else if (ids[0] == "stages")
            {
                action = "select * from stage where s_name not in (select s_name from projectxstage where p_id = " + ids[1] + ");";
            }
            else
            {
                action = "SELECT * FROM stage;";
            }
            System.Diagnostics.Debug.WriteLine("print4");
            System.Diagnostics.Debug.WriteLine(action);
            var command = new NpgsqlCommand(action, myConnection);
            System.Diagnostics.Debug.WriteLine("print5");
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            while (coso.Read())
            {
                stag = new Stage();
                stag.s_name = (string)coso["s_name"];
                stag.s_description = (string)coso["s_description"];
                values.Add(stag);
            }

            myConnection.Close();
            return Json(values);

        }
    }
}
