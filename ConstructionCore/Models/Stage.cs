using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class Stage
    {
        public string s_name { get; set; }
        public string s_status { get; set; }
        public DateTime s_datestart { get; set; }
        public DateTime s_dateend { get; set; }
        public string s_description { get; set; }
        public int s_budget { get; set; }
    }
}