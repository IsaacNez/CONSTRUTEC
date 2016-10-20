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
    public class ProjectDetailsController : ApiController
    {
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
            string test = "select * from getprojectdetails(" + ids[0] + ");";
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
                projstage stag = new projstage();
                stag.s_name = (string)coso["gpd_sname"];
                stag.s_datestart = (DateTime)coso["gpd_datestart"];
                stag.s_dateend = (DateTime)coso["gpd_dateend"];
                stag.s_status = (String)coso["gpd_status"];
                if (proj.stages.IndexOf(stag)!=-1)
                {
                    System.Diagnostics.Debug.WriteLine("entro al true");
                    int i = proj.stages.IndexOf(stag);
                    projstage tmp = proj.stages.ElementAt(i);
                    prodstage product = new prodstage();
                    product.p_id = (int)coso["gpd_pid"];
                    product.p_price = (int)coso["gpd_price"];
                    product.p_quantity = (int)coso["gpd_quantity"];
                    proj.stages.ElementAt(i).products.Add(product);
                }
                else if(proj.stages.IndexOf(stag)==-1) {
                    System.Diagnostics.Debug.WriteLine("entro al false");

                    proj.stages.Add(stag);
                    int i = proj.stages.IndexOf(stag);
                    prodstage product = new prodstage();
                    product.p_id = (int)coso["gpd_pid"];
                    product.p_price = (int)coso["gpd_price"];
                    product.p_quantity = (int)coso["gpd_quantity"];
                    proj.stages.ElementAt(i).products.Add(product);
                }
            }
            values.Add(proj);
            myConnection.Close();
            return Json(values);

        }
    }
}
