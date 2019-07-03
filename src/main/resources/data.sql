/*
INSERT INTO service_host_mapping (id,service_name,host) VALUES ('1','customerservice','http://localhost:8081'),
('2','bankservice','http://localhost:8082'),
('3','orderservice','http://localhost:8083');
INSERT INTO saga_command (id,command) VALUES (1,'postElectronicOrder'),
(2,'updateElectronicOrder'),
(3,'postOrder');
INSERT  INTO saga_step (id,end_point_name,service_name,build_json_from,build_json_to) VALUES (1,'post.customer','customer','json','customer'),
(2,'post.bank','bank','customer','bank'),
(3,'post.order','order','bank','order'),
(4,'update.customer','customer','json','customer'),
(5,'update.bank','bank','customer','bank'),
(6,'update.order','order','bank','order'),
(7,'post.customer','customer','json','customer'),
(8,'post.order','order','customer','order');
INSERT INTO saga_command_saga_step_list(saga_command_id, saga_step_list_id) VALUES (1,1),(1,2),(1,3),(2,4),(2,5),(2,6),(3,1),(3,8);*/