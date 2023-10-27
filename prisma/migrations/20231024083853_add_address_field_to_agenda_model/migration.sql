/*
  Warnings:

  - Added the required column `address` to the `Agenda` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `Agenda` ADD COLUMN `address` VARCHAR(191) NOT NULL;

-- AlterTable
ALTER TABLE `News` ADD COLUMN `active` BOOLEAN NOT NULL DEFAULT true;
