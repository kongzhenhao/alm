import { test, expect } from '@playwright/test';

/**
 * 负债现金流页面测试
 * 测试web/src/views/dur/liability/cash/flow/index.vue页面的功能
 */

// 设置cookie方式登录
async function loginWithCookie(context) {
  await context.addCookies([
    {
      name: 'Admin-Token',
      value: 'eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjIzMjAzNTM2LWM4NDYtNDdmZC04YjljLWM2OWQ5YmIyMjNjMyJ9.OwTUB4YwSo9T5AlpLN3uWcZDWfubXwTCPIe9qGhKOT0tZCRJaKzgHSE4rDTZJt1xyLnvPSVMpRbkdhpqCBv-oA',
      domain: 'alm-int.hongkang-life.com',
      path: '/'
    }
  ]);
}

test.describe('负债现金流页面测试', () => {

  // 每个测试前都执行登录
  test.beforeEach(async ({ page, context }) => {
    // 使用cookie方式登录
    await loginWithCookie(context);

    // 导航到负债现金流页面
    await page.goto('https://alm-int.hongkang-life.com/web/#/dur/liabilityCashFlow');

    // 等待页面加载完成
    await page.waitForSelector('.app-container');
  });

//  test('TC-002: 搜索功能测试', async ({ page }) => {
//    // 输入搜索条件
//    await page.fill('input[placeholder="请输入账期"]', '202412');
//
//    // 点击搜索按钮
//    await page.click('button:has-text("搜索")');
//
//    // 等待搜索结果加载
//    await page.waitForResponse(response =>
//      response.url().includes('/dur/liability/cash/flow/list') &&
//      response.status() === 200
//    );
//
//    // 验证表格是否显示搜索结果
//    // 注意：这里的断言可能需要根据实际数据调整
//    await expect(page.locator('.el-table')).toBeVisible();
//  });
//
//  test('TC-003: 重置功能测试', async ({ page }) => {
//    // 输入搜索条件
//    await page.fill('input[placeholder="请输入账期"]', '202401');
//    await page.fill('input[placeholder="请输入精算代码"]', 'TEST');
//
//    // 点击重置按钮
//    await page.getByRole('button').filter({ hasText: /^重置$/ }).click();
//    // await page.locator('button').filter({ hasText: /^重置$/ }).click();
//    // await page.getByRole('button', { name: /^重置$/ }).click();
//
//    // 验证输入框是否被清空
//    await expect(page.locator('input[placeholder="请输入账期"]')).toHaveValue('');
//    await expect(page.locator('input[placeholder="请输入精算代码"]')).toHaveValue('');
//  });

  test('TC-004: 新增功能测试', async ({ page }) => {
    // 点击新增按钮
    await page.click('button:has-text("新增")');

    // 验证新增对话框是否显示
    await expect(page.locator('.el-dialog__title:has-text("添加负债现金流")')).toBeVisible();

    // 填写表单

    await page.fill('input[placeholder="请输入账期，格式YYYYMM"]', '202401');
    const dialog = await page.getByRole('dialog', { name: '添加负债现金流' });
    await dialog.getByPlaceholder('请选择现金流类型').click();
    await page.getByRole('listitem').filter({ hasText: '流入' }).click();
    await dialog.getByPlaceholder('请选择基点类型').click();
    await page.getByRole('listitem').filter({ hasText: '+50bp' }).click();
    await dialog.getByPlaceholder('请选择久期类型').click();
    await page.getByRole('listitem').filter({ hasText: '修正久期' }).click();
    await dialog.getByPlaceholder('请选择是否中短期险种').click();
    await page.getByRole('listitem').filter({ hasText: '否' }).click();

    await dialog.getByPlaceholder('请输入设计类型').fill('测试设计类型');
    await dialog.getByPlaceholder('请输入精算代码').fill('TEST001');
    await dialog.getByPlaceholder('请输入业务代码').fill('BIZ001');
    await dialog.getByPlaceholder('请输入产品名称').fill('测试产品');
    await dialog.getByPlaceholder('请输入险种主类').fill('测试主类');
    await dialog.getByPlaceholder('请输入险种细类').fill('测试细类');
    await dialog.getByPlaceholder('请输入现金流值集，JSON格式').fill('{"1":{"date":"2024-01-01","val":100}}');

    // 点击确定按钮
    // 注意：这里只模拟点击，不实际提交，避免影响实际数据
     await dialog.locator('.el-dialog__footer button:has-text("确 定")').click();

    // 点击取消按钮关闭对话框
    // await dialog.locator('.el-dialog__footer button:has-text("取 消")').click();

    // 验证对话框是否关闭
    await expect(page.locator('.el-dialog__title:has-text("添加负债现金流")')).not.toBeVisible();
  });

//  test('TC-006: 查看现金流值集功能测试', async ({ page }) => {
//    // 等待表格数据加载
//    await page.waitForSelector('.el-table__row');
//
//    // 如果表格中有数据，点击第一行的"查看"按钮
//    const hasRows = await page.locator('.el-table__row').count() > 0;
//
//    if (hasRows) {
//      // 点击第一行的"查看"按钮
//      await page.click('.el-table__row:first-child button:has-text("查看")');
//
//      // 验证现金流值集对话框是否显示
//      await expect(page.locator('.el-dialog__title:has-text("现金流值集查看")')).toBeVisible();
//
//      // 检查表格是否显示
//      await expect(page.locator('.el-dialog .el-table')).toBeVisible();
//
//      // 点击关闭按钮
//      await page.click('.el-dialog__footer button:has-text("关 闭")');
//
//      // 验证对话框是否关闭
//      await expect(page.locator('.el-dialog__title:has-text("现金流值集查看")')).not.toBeVisible();
//    } else {
//      // 如果没有数据，跳过此测试
//      test.skip(true, '表格中没有数据，无法测试查看功能');
//    }
//  });
});
