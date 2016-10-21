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

    public class CostumerserviceprodController : ApiController
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
            string test = "select * from getcustomerservicebyproductanddate('" + ids[0] + "','" + ids[1] + "');";
            System.Diagnostics.Debug.WriteLine(test);

            var command = new NpgsqlCommand(test, myConnection);
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            
            while (coso.Read())
            {
                System.Diagnostics.Debug.WriteLine((DateTime)coso["csp_datestart"]);

                cu = new generaluser();
                cu.gcs_datestart = (DateTime)coso["csp_datestart"];

                cu.gcs_pid = (int)coso["csp_pid"];

                cu.gcs_plocation = (string)coso["csp_plocation"];
                cu.gcs_sname = (string)coso["csp_sname"];
                cu.gcs_uid = (int)coso["csp_uid"];
                cu.gcs_uname = (string)coso["csp_uname"];
                cu.gcs_uphone = (int)coso["csp_uphone"];
                values.Add(cu);
            }

            myConnection.Close();
            return Json(values);

        }
    }
}
