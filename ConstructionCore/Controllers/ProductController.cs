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
    public class ProductController : ApiController
    {
        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<Product>> Get(string attribute, string id)
        {
            Product prod = null;
            List<Product> values = new List<Product>();
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
                var need = new UserController();
                action = need.FormConnectionString("product", attr, ids);
            }
            else
            {
                action = "SELECT * FROM product;";
            }
            System.Diagnostics.Debug.WriteLine("print4");
            System.Diagnostics.Debug.WriteLine(action);
            var command = new NpgsqlCommand(action, myConnection);
            System.Diagnostics.Debug.WriteLine("print5");
            var coso = command.ExecuteReader();
            System.Diagnostics.Debug.WriteLine("print6");
            while (coso.Read())
            {
                prod = new Product();
                prod.pr_id = (int)coso["pr_id"];
                prod.pr_name = (string)coso["pr_name"];
                prod.pr_description = (string)coso["pr_description"];
                prod.pr_price = (int)coso["pr_price"];
                prod.pr_quantity = (int)coso["pr_quantity"];
                
                values.Add(prod);
            }

            myConnection.Close();
            return Json(values);

        }
    }
}
