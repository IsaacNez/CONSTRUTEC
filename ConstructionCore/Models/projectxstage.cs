using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class projectxstage
    {
        public int pxs_id { get; set; }
        public int p_id { get; set; }
        public string s_name { get; set; }

        public DateTime pxs_datestart { get; set; }
        public DateTime pxs_dateend { get; set; }
        public int pxs_budget { get; set; }
        public string pxs_status { get; set; }


    }
}