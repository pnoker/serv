﻿-- ----------------------------
-- Records of SERVE_CONFIG
-- ----------------------------
INSERT INTO "SERVE_CONFIG" VALUES ('0', null, null, '根目录', 'tujyt', null, '0', '0', null, null, null, null, null, null, '0', null, null, null, null, TO_TIMESTAMP(' 2016-04-11 13:38:02:971000', 'YYYY-MM-DD HH24:MI:SS:FF6'), null, null, '0', null, null, TO_TIMESTAMP(' 2016-04-11 13:38:02:971000', 'YYYY-MM-DD HH24:MI:SS:FF6').null,null);

UPDATE "SERVE_CONFIG" SET ID=0 WHERE SERVICE_NAME='根目录';

INSERT INTO "SERVE_CONFIG" VALUES ('1', '0', null, '内部服务', 'sdagd', '1', '1', '0', null, null, '这是目录根节点 ', null, null, null, '0', null, null, null, null, TO_TIMESTAMP(' 2016-03-09 16:21:14:193000', 'YYYY-MM-DD HH24:MI:SS:FF6'), null, null, '0', null, null, TO_TIMESTAMP(' 2016-04-27 15:40:58:529000', 'YYYY-MM-DD HH24:MI:SS:FF6'),null,null);
INSERT INTO "SERVE_CONFIG" VALUES ('2', '0', null, '外部服务', 'we', '2', '1', '0', null,null, null, null, null, null, '0', null, null, null, null, TO_TIMESTAMP(' 2016-04-11 13:39:08:026000', 'YYYY-MM-DD HH24:MI:SS:FF6'), null, null, '0', null, null, TO_TIMESTAMP(' 2016-04-11 13:39:08:026000', 'YYYY-MM-DD HH24:MI:SS:FF6'),null,null);
INSERT INTO "SERVE_CONFIG" VALUES ('3', '0', null, 'GIS服务', 'dsgcv', '3', '1', '0', null,null, '12331是111信息ss', null, null, null, '0', null, null, null, null, TO_TIMESTAMP(' 2016-04-11 13:39:08:074000', 'YYYY-MM-DD HH24:MI:SS:FF6'), null, null, '0', null, null, TO_TIMESTAMP(' 2016-04-27 13:22:25:432000', 'YYYY-MM-DD HH24:MI:SS:FF6'),null,null);

UPDATE "SERVE_CONFIG" SET ID=1 WHERE SERVICE_NAME='内部服务';
UPDATE "SERVE_CONFIG" SET ID=2 WHERE SERVICE_NAME='外部服务';
UPDATE "SERVE_CONFIG" SET ID=3 WHERE SERVICE_NAME='GIS服务';
