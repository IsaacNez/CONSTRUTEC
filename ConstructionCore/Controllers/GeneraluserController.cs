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
    public class GeneraluserController : ApiController
    {
        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<generaluser>> Get(string attribute, string id)
        {
            generaluser cu = null;
            List<generaluser> values = new List<generaluser>();
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            System.Diagnostics.Debug.WriteLine("print1");
            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            myConnection.Open();
            string test = "";
            if (ids.Length <= 1)
            {
                test = "select * from getcustomerservicebydate('" + ids[0] + "');";
                System.Diagnostics.Debug.WriteLine(test);

            }
            else
            {
                test = "select * from getcustomerservicebyproductanddate('" + ids[0] + "','" + ids[1] + "');";
                System.Diagnostics.Debug.WriteLine(test);


            }

            var command = new NpgsqlCommand(test, myConnection);
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            while (coso.Read())
            {
                cu = new generaluser();

                System.Diagnostics.Debug.WriteLine((DateTime)coso["gcs_datestart"]);


                cu.gcs_datestart = (DateTime)coso["gcs_datestart"];
                cu.gcs_pid = (int)coso["gcs_pid"];
                cu.gcs_plocation = (string)coso["gcs_plocation"];
                cu.gcs_sname = (string)coso["gcs_sname"];
                cu.gcs_uid = (int)coso["gcs_uid"];
                cu.gcs_uname = (string)coso["gcs_uname"];
                cu.gcs_status = (string)coso["gcs_status"];
                cu.gcs_uphone = (int)coso["gcs_uphone"];
                values.Add(cu);
            }

            myConnection.Close();
            return Json(values);

        }
    }
}
