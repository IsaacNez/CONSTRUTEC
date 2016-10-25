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
    public class ProjectxstageController : ApiController
    {

       
        [HttpPost]
        [ActionName("Post")]
        public void AddStage(projectxstage stage)
        {
            System.Diagnostics.Debug.WriteLine("print1");
            System.Diagnostics.Debug.WriteLine(stage.s_name);

            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "INSERT INTO projectxstage(p_id,s_name, pxs_datestart, pxs_dateend,pxs_status) Values(:p_id,:s_name,:pxs_datestart,:pxs_dateend,:pxs_status)";
            
            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.Text;
            System.Diagnostics.Debug.WriteLine("print5");
            System.Diagnostics.Debug.WriteLine("generando comando");
            command.Parameters.AddWithValue(":p_id", stage.p_id);
            command.Parameters.AddWithValue(":s_name", stage.s_name);
            command.Parameters.AddWithValue(":pxs_status", stage.pxs_status);
            command.Parameters.AddWithValue(":pxs_datestart", stage.pxs_datestart);
            command.Parameters.AddWithValue(":pxs_dateend", stage.pxs_dateend);
            myConnection.Open();
            command.ExecuteNonQuery();
            System.Diagnostics.Debug.WriteLine("print6");
            myConnection.Close();
            
        }

        [HttpGet]
        [ActionName("Update")]
        public void UpdateStatus(string attribute, string id)
        {
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            string action = "update projectxstage set pxs_status ="+ids[0]+" where p_id =" +ids[1] +" and s_name="+ids[2];
            NpgsqlConnection myCon = new NpgsqlConnection(); 
            myCon.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            var command = new NpgsqlCommand(action, myCon);
            myCon.Open();
            command.ExecuteNonQuery();

        }

        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<projectxstage>> Get(string attribute, string id)
        {
            projectxstage stage = null;
            List<projectxstage> values = new List<projectxstage>();
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
                action = need.FormConnectionString("projectxstage", attr, ids);
            }
            else
            {
                action = "SELECT * FROM projectxstage;";
            }
            System.Diagnostics.Debug.WriteLine("print4");
            System.Diagnostics.Debug.WriteLine(action);
            var command = new NpgsqlCommand(action, myConnection);
            System.Diagnostics.Debug.WriteLine("print5");
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            while (coso.Read())
            {
                stage = new projectxstage();
                stage.pxs_id = (int)coso["pxs_id"];
                stage.pxs_status = (string)coso["pxs_status"];
                stage.p_id = (int)coso["p_id"];
                stage.s_name = (string)coso["s_name"];
                stage.pxs_datestart = (DateTime)coso["pxs_datestart"];
                stage.pxs_dateend = (DateTime)coso["pxs_dateend"];
                stage.pxs_budget = (int)coso["pxs_budget"];
                values.Add(stage);
            }

            myConnection.Close();
            return Json(values);

        }

    }
}
