import Layout from '@/layout';

export default {
  path: '/dur',
  component: Layout,
  redirect: '/dur/cashFlow',
  name: 'Duration',
  meta: {
    title: '久期管理',
    icon: 'chart'
  },
  children: [
    {
      path: 'cashFlow',
      component: () => import('@/views/dur/cashFlow/index'),
      name: 'CashFlow',
      meta: { title: '负债现金流', icon: 'money' }
    },
    {
      path: 'discountFactor',
      component: () => import('@/views/dur/discountFactor/index'),
      name: 'DiscountFactor',
      meta: { title: '折现因子', icon: 'calculator' }
    },
    {
      path: 'discountCurve',
      component: () => import('@/views/dur/discountCurve/index'),
      name: 'DiscountCurve',
      meta: { title: '折现曲线', icon: 'chart' }
    }
  ]
}
