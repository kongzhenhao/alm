import Vue from "vue";
import SvgIcon from "@/components/SvgIcon";
Vue.component("svg-icon", SvgIcon);

const re = /\.\/(.*)\.svg/;
const AllSvgIcon = require
  .context("./svg", false, /\.svg$/)
  .keys()
  .map((i) => {
    return i.match(re)[1];
  });
Vue.prototype.AllSvgIcon = AllSvgIcon;
