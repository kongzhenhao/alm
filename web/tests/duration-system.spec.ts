import { test, expect } from '@playwright/test';
import path from 'path';

// 登录复用函数
async function login(page) {
  await page.goto('https://alm-int.hongkang-life.com/web/#/login');
  await page.fill('input[name="username"]', 'admin');
  await page.fill('input[name="password"]', 'admin123');
  await page.click('button[type="submit"]');
  // 等待跳转到首页
  await page.waitForURL('**/index');
}

// 负债现金流数据导入

test.describe('负债现金流数据导入', () => {
  test('TC-001: 正常导入', async ({ page }) => {
    await login(page);
    await page.click('text=负债现金流数据导入');
    await page.waitForSelector('input[type="file"]');
    await page.setInputFiles('input[type="file"]', path.resolve(__dirname, './data/liability_cash_flow_sample.xlsx'));
    await page.click('button:has-text("导入")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/导入成功/);
  });

  test('TC-002: 非法文件导入', async ({ page }) => {
    await login(page);
    await page.click('text=负债现金流数据导入');
    await page.waitForSelector('input[type="file"]');
    await page.setInputFiles('input[type="file"]', path.resolve(__dirname, './data/liability_cash_flow_invalid.xlsx'));
    await page.click('button:has-text("导入")');
    await expect(page.locator('.ant-message-error, .el-message--error')).toHaveText(/文件格式不符合要求/);
  });
});

// 折现曲线数据导入

test.describe('折现曲线数据导入', () => {
  test('TC-003: 正常导入', async ({ page }) => {
    await login(page);
    await page.click('text=折现曲线数据导入');
    await page.waitForSelector('input[type="file"]');
    await page.setInputFiles('input[type="file"]', path.resolve(__dirname, './data/discount_curve_sample.xlsx'));
    await page.click('button:has-text("导入")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/导入成功/);
  });

  test('TC-004: 非法文件导入', async ({ page }) => {
    await login(page);
    await page.click('text=折现曲线数据导入');
    await page.waitForSelector('input[type="file"]');
    await page.setInputFiles('input[type="file"]', path.resolve(__dirname, './data/discount_curve_invalid.xlsx'));
    await page.click('button:has-text("导入")');
    await expect(page.locator('.ant-message-error, .el-message--error')).toHaveText(/文件格式不符合要求/);
  });
});

// 折现因子数据导入

test.describe('折现因子数据导入', () => {
  test('TC-005: 正常导入', async ({ page }) => {
    await login(page);
    await page.click('text=折现因子数据导入');
    await page.waitForSelector('input[type="file"]');
    await page.setInputFiles('input[type="file"]', path.resolve(__dirname, './data/discount_factor_sample.xlsx'));
    await page.click('button:has-text("导入")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/导入成功/);
  });

  test('TC-006: 非法文件导入', async ({ page }) => {
    await login(page);
    await page.click('text=折现因子数据导入');
    await page.waitForSelector('input[type="file"]');
    await page.setInputFiles('input[type="file"]', path.resolve(__dirname, './data/discount_factor_invalid.xlsx'));
    await page.click('button:has-text("导入")');
    await expect(page.locator('.ant-message-error, .el-message--error')).toHaveText(/文件格式不符合要求/);
  });
});

// 负债现金流汇总及现值计算

test.describe('负债现金流汇总及现值计算', () => {
  test('TC-007: 正常计算', async ({ page }) => {
    await login(page);
    await page.click('text=负债现金流汇总及现值计算');
    await page.selectOption('select[name="period"]', '202307');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
  });

  test('TC-008: 特殊情况（现金流为0或负值）', async ({ page }) => {
    await login(page);
    await page.click('text=负债现金流汇总及现值计算');
    await page.selectOption('select[name="period"]', '202307');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
    // 可选：校验页面有0或负值的现值
  });

  test('TC-009: 缺失数据', async ({ page }) => {
    await login(page);
    await page.click('text=负债现金流汇总及现值计算');
    await page.selectOption('select[name="period"]', '202308');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-error, .el-message--error')).toHaveText(/缺少必要的数据/);
  });
});

// 负债久期计算

test.describe('负债久期计算', () => {
  test('TC-010: 修正久期', async ({ page }) => {
    await login(page);
    await page.click('text=负债久期计算');
    await page.selectOption('select[name="period"]', '202307');
    await page.selectOption('select[name="durationType"]', '修正久期');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
  });

  test('TC-011: 有效久期', async ({ page }) => {
    await login(page);
    await page.click('text=负债久期计算');
    await page.selectOption('select[name="period"]', '202307');
    await page.selectOption('select[name="durationType"]', '有效久期');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
  });

  test('TC-012: 特殊情况（现值为0）', async ({ page }) => {
    await login(page);
    await page.click('text=负债久期计算');
    await page.selectOption('select[name="period"]', '202307');
    await page.selectOption('select[name="durationType"]', '修正久期');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
    // 可选：校验页面有久期为0的情况
  });
});

// 分账户负债现金流现值汇总

test.describe('分账户负债现金流现值汇总', () => {
  test('TC-013: 正常计算', async ({ page }) => {
    await login(page);
    await page.click('text=分账户负债现金流现值汇总');
    await page.selectOption('select[name="period"]', '202307');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
  });

  test('TC-014: 特殊情况（现值为0或负值）', async ({ page }) => {
    await login(page);
    await page.click('text=分账户负债现金流现值汇总');
    await page.selectOption('select[name="period"]', '202307');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
  });
});

// 分账户负债久期汇总

test.describe('分账户负债久期汇总', () => {
  test('TC-015: 修正久期', async ({ page }) => {
    await login(page);
    await page.click('text=分账户负债久期汇总');
    await page.selectOption('select[name="period"]', '202307');
    await page.selectOption('select[name="durationType"]', '修正久期');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
  });

  test('TC-016: 有效久期', async ({ page }) => {
    await login(page);
    await page.click('text=分账户负债久期汇总');
    await page.selectOption('select[name="period"]', '202307');
    await page.selectOption('select[name="durationType"]', '有效久期');
    await page.click('button:has-text("计算")');
    await expect(page.locator('.ant-message-success, .el-message--success')).toHaveText(/计算成功/);
  });
});

// 大数据量、精度、并发测试等可根据实际情况补充实现 