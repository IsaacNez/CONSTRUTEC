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
    public class ProjectController : ApiController
    {
        [HttpPost]
        [ActionName("Post")]
        public void AddProject(Project proj)
        {
            System.Diagnostics.Debug.WriteLine("print1");
            System.Diagnostics.Debug.WriteLine(proj.p_name);

            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "INSERT INTO project(p_id,p_location,p_name, p_budget, u_code, u_id) Values(:p_id,:p_location,:p_name,:p_budget,:u_code, :u_id)";

            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.Text;
            System.Diagnostics.Debug.WriteLine("print5");


            System.Diagnostics.Debug.WriteLine("generando comando");


            command.Parameters.AddWithValue(":p_id", proj.p_id);
            command.Parameters.AddWithValue(":p_location", proj.p_location);
            command.Parameters.AddWithValue(":p_name", proj.p_name);
            command.Parameters.AddWithValue("p_budget", proj.p_budget);
            command.Parameters.AddWithValue("u_code", proj.u_code);
            command.Parameters.AddWithValue("u_id", proj.u_id);
            myConnection.Open();
            command.ExecuteNonQuery();

            System.Diagnostics.Debug.WriteLine("print6");


            command.ExecuteReader();
            myConnection.Close();

        }
        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<ProjectDetails>> Get(string attribute, string id)
        {
            ProjectDetails proj = null;
            List<ProjectDetails> values = new List<ProjectDetails>();
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            System.Diagnostics.Debug.WriteLine("print1");
            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "select * from getprojectdetails(" + ids[0]+ "');";
            System.Diagnostics.Debug.WriteLine(test);
            var command = new NpgsqlCommand(test, myConnection);
            myConnection.Open();
            var coso = command.ExecuteReader();
            proj = new ProjectDetails();
            while (coso.Read())
            {
                proj.gpd_id = (int)coso["gpd_id"];
                proj.gpd_name = (string)coso["gpd_name"];
                proj.gpd_location = (string)coso["gpd_location"];
                proj.gpd_engineer = (int)coso["gpd_engineer"];
                proj.gpd_owner = (int)coso["gpd_owner"];
                
                
                

                


      
            }
            values.Add(proj);
            myConnection.Close();
            return Json(values);

        }


    }
}
