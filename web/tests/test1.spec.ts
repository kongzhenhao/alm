import { test, expect } from '@playwright/test';

test('test', async ({ page, context }) => {

  test.setTimeout(20_000);

  // 设置认证Token cookie
  const domain = 'alm-int.hongkang-life.com';
  const tokenValue = 'eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImNjMzQ5NWFhLTU2MTktNGZlMy1iYzU1LTM5MzBlY2E5NWZmMyJ9.tnGdM0g4iAJ_SIdAjYdjGbRNppDf5VMn4CHQvVrcFcSPnAe-PmHEm8MpOH8alEVDYLQ6z6z8lsBngbgmhR884A';

  // 使用context.addCookies()方法设置cookie
  await context.addCookies([
    {
      name: 'Admin-Token',
      value: tokenValue,
      domain: domain,        // 明确设置cookie的域名
      path: '/'            // cookie的有效路径
    }
  ]);
  await page.goto('https://alm-int.hongkang-life.com/web/#/index');
  await page.locator('div').filter({ hasText: /^保险相关$/ }).click();
  await page.getByRole('link', { name: '保单续保率统计' }).click();
  await page.getByRole('textbox', { name: '请输入账期，格式YYYYMM' }).fill('202412');
  await page.getByRole('button', { name: ' 统计计算' }).click();
  await page.getByRole('button', { name: '确定' }).click();
});
