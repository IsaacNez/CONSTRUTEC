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
    public class ClientController : ApiController
    {
        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<Engineer>> Get(string attribute, string id)
        {
            Engineer eng = null;
            List<Engineer> values = new List<Engineer>();
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            System.Diagnostics.Debug.WriteLine("print1");
            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            myConnection.Open();
            string action = "select dbuser.u_id,dbuser.u_name from dbuser where dbuser.u_name <> 'Admin';";
            var command = new NpgsqlCommand(action, myConnection);
            System.Diagnostics.Debug.WriteLine("print5");
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            while (coso.Read())
            {
                eng = new Engineer();
                eng.u_id = (int)coso["u_id"];
                eng.u_name = (string)coso["u_name"];
                values.Add(eng);
            }

            myConnection.Close();
            return Json(values);

        }
    }
}
