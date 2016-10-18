﻿using System;
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
    }
}
