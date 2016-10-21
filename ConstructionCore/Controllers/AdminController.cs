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
using System.Data.SqlClient;
namespace ConstructionCore.Controllers
{
    [EnableCors(origins: "*", headers: "*", methods: "*")]

    public class AdminController : ApiController
    {
        static private string GetConnectionString()
        {
            return @"Data Source=DESKTOP-E6QPTVT;Initial Catalog=EPATEC;"
                + "Integrated Security=true;";
        }

        [HttpGet]
        [ActionName("Get")]
        public JsonResult<List<Product>> Get(string attribute, string id)
        {
            string[] attr = attribute.Split(',');
            string[] ids = id.Split(',');
            List<Product> values = new List<Product>();
            Product prod = null;

            System.Diagnostics.Debug.WriteLine("entrando al get");
            SqlDataReader reader = null;
            SqlConnection myConnection = new SqlConnection();
            myConnection.ConnectionString = GetConnectionString();
            System.Diagnostics.Debug.WriteLine("cargo base");
            SqlCommand sqlCmd = new SqlCommand();
            System.Diagnostics.Debug.WriteLine("cargo sqlcommand");
            string action = "";

            if (id != "undefined")
            {
                var constructor = new UserController();
                action = constructor.FormConnectionString("PRODUCT", attr, ids);
            }
            else
            {
                action = "SELECT * FROM PRODUCT;";
            }

            System.Diagnostics.Debug.WriteLine(action);
            sqlCmd.CommandType = CommandType.Text;
            sqlCmd.CommandText = action;
            System.Diagnostics.Debug.WriteLine("cargo comando");

            sqlCmd.Connection = myConnection;
            myConnection.Open();
            System.Diagnostics.Debug.WriteLine("estado " + myConnection.State);

            var coso = sqlCmd.ExecuteReader();

            while (coso.Read())
            {
                prod = new Product();
                prod.pr_id = (int)coso["P_ID"];
                prod.pr_name = (string)coso["PName"];
                prod.pr_description = (string)coso["PDescription"];
                prod.pr_price = (int)coso["Price"];
                prod.pr_quantity = (int)coso["Quantity"];
                AddProduct(prod);
                values.Add(prod);
            }

            myConnection.Close();
            return Json(values);

        }
        public void AddProduct(Product product)
        {
            System.Diagnostics.Debug.WriteLine("print1");
            System.Diagnostics.Debug.WriteLine(product.pr_name);

            NpgsqlConnection myConnection = new NpgsqlConnection();
            System.Diagnostics.Debug.WriteLine("print2");
            myConnection.ConnectionString = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString;
            System.Diagnostics.Debug.WriteLine("print3");
            System.Diagnostics.Debug.WriteLine("cargo base");
            string test = "INSERT INTO product(pr_id,pr_description,pr_name,pr_price,pr_quantity) Values(:pr_id,:pr_description,:pr_name,:pr_price,:pr_quantity)";

            var command = new NpgsqlCommand(test, myConnection);
            command.CommandType = CommandType.Text;
            System.Diagnostics.Debug.WriteLine("print5");


            System.Diagnostics.Debug.WriteLine("generando comando");



            command.Parameters.AddWithValue(":pr_id", product.pr_id);
            command.Parameters.AddWithValue(":pr_description", product.pr_description);
            command.Parameters.AddWithValue(":pr_name", product.pr_name);
            command.Parameters.AddWithValue(":pr_price", product.pr_price);
            command.Parameters.AddWithValue(":pr_quantity", product.pr_quantity);

            myConnection.Open();
            command.ExecuteNonQuery();
            System.Diagnostics.Debug.WriteLine("print6");
            myConnection.Close();


        }
    }
}
