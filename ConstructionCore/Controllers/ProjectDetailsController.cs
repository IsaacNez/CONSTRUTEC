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
    public class ProjectDetailsController : ApiController
    {
        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<ProjectDetails>> Get(string attribute, string id)
        {
            ProjectDetails proj = null;
            List<ProjectDetails> values = new List<ProjectDetails>();
            string[] ids = id.Split(',');
            NpgsqlConnection myConnection = new NpgsqlConnection();
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            string test = "select * from getprojectdetails(" + ids[0] + ");";         
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
                proj.gdp_pbudget = (int)coso["gpd_pbudget"];
                proj.gpd_owner = (int)coso["gpd_owner"];
                projstage stag = new projstage();
                if (coso["gpd_sname"].ToString().Length>1) {
                    stag.s_name = (string)coso["gpd_sname"];
                    stag.s_datestart = (DateTime)coso["gpd_datestart"];
                    stag.gpd_budget = (int)coso["gpd_budget"];
                    stag.s_dateend = (DateTime)coso["gpd_dateend"];
                    stag.s_status = (String)coso["gpd_status"];
                    if (proj.stages.Count != 0)
                    {
                        for (int z = 0; z < proj.stages.Count; z++)
                        {
                            if (proj.stages.ElementAt(z).s_name == stag.s_name)
                            {
                                prodstage product = new prodstage();
                                product.p_name = (string)coso["gpd_prname"];
                                product.p_id = (int)coso["gpd_pid"];
                                product.p_price = (int)coso["gpd_price"];
                                product.p_quantity = (int)coso["gpd_quantity"];
                                proj.stages.ElementAt(z).products.Add(product);
                            }
                            else if (proj.stages.ElementAt(z).s_name != stag.s_name && z == proj.stages.Count - 1)
                            {
                                proj.stages.Add(stag);
                                int i = proj.stages.IndexOf(stag);
                                prodstage product = new prodstage();
                                product.p_id = (int)coso["gpd_pid"];
                                product.p_name = (string)coso["gpd_prname"];
                                product.p_price = (int)coso["gpd_price"];
                                product.p_quantity = (int)coso["gpd_quantity"];
                                proj.stages.ElementAt(i).products.Add(product);
                            }
                        }
                    }
                    else if (proj.stages.Count == 0)
                    {
                        proj.stages.Add(stag);
                        int i = proj.stages.IndexOf(stag);
                        prodstage product = new prodstage();
                        product.p_id = (int)coso["gpd_pid"];
                        product.p_name = (string)coso["gpd_prname"];
                        product.p_price = (int)coso["gpd_price"];
                        product.p_quantity = (int)coso["gpd_quantity"];
                        proj.stages.ElementAt(i).products.Add(product);
                    }                                 
                }               
                else{
                    myConnection.Close();
                    values.Add(proj);                   
                }
            }
            values.Add(proj);
            myConnection.Close();
            return Json(values);
        }
    }
}
