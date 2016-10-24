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
using System.Data.SqlClient;
namespace ConstructionCore.Controllers
{
    public class OrderController : ApiController
    {
        [HttpPost]
        [ActionName("Post")]
        public void AddOrder(Order order)
        {
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri("http://desktop-e6qptvt:7549/");
            client.PutAsJsonAsync("api/Order/post/", order).ContinueWith((postTask) => postTask.Result.EnsureSuccessStatusCode());
        }
    }
            

    }

