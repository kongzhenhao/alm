<template>
  <div
    :class="{ 'has-logo': showLogo }"
    :style="{
      backgroundColor:
        settings.sideTheme === 'theme-dark'
          ? variables.menuBackground
          : variables.menuLightBackground,
    }"
  >
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
      <el-menu
        ref="menu"
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="
          settings.sideTheme === 'theme-dark'
            ? variables.menuBackground
            : variables.menuLightBackground
        "
        :text-color="
          settings.sideTheme === 'theme-dark'
            ? variables.menuColor
            : variables.menuLightColor
        "
        :unique-opened="true"
        :active-text-color="settings.theme"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import Logo from './Logo';
import SidebarItem from './SidebarItem';
import variables from '@/assets/styles/variables.scss';

export default {
  componentName: 'Sidebar',
  components: { SidebarItem, Logo },
  computed: {
    ...mapState(['settings']),
    ...mapGetters(['sidebarRouters', 'sidebar']),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    },
  },
  created() {
    this.initEvent();
  },
  methods: {
    initEvent() {
      this.$on('change', this.onChange.bind(this));
    },
    onChange(isLast) {
      setTimeout(() => {
        if (isLast) {
          this.clickLastMenu();
        } else {
          this.clickFirstMenu();
        }
      });
    },
    // 点击第一个菜单
    clickFirstMenu() {
      this.$refs?.menu?.$children?.[1]?.$children[0]?.$el?.click();
    },
    // 点击最后一个菜单
    clickLastMenu() {
      const { length = 0 } = this.$refs?.menu?.$children;
      if (length) {
        this.$refs?.menu?.$children?.[length - 1]?.$children[0]?.$el?.click();
      }
    },
  },
};
</script>
