<h3>Member Controller</h3>
- <b>POST</b> : "/api/v1/member" <br>
<b>Description</b> >> 직원 등록 <br>
<b>Body</b> >> name : String, role : String, birthday : LocalDate, workStartDate : LocalDate <br><br>
- <b>GET</b> : "/api/v1/member-list" <br>
<b>Description</b> >> 모든 직원 정보 조회 <br><br>
- <b>GET</b> : "/api/v1/member/{membmerId}" <br>
<b>Description</b> >> 특정 직원 정보 조회 <br><br>
<br>
<h3>Team Controller</h3>
- <b>POST</b> : "/api/v1/team" <br>
<b>Description</b> >> 팀 등록 <br>
<b>Body</b> >> name : String, leavePolicy : int <br><br>
- <b>GET</b> : "/api/v1/team" <br>
<b>Description</b> >> 모든 팀 정보 조회 <br><br>
- <b>GET</b> : "/api/v1/team/{teamId}" <br>
<b>Description</b> >> 특정 팀 정보 조회 <br><br>
<br>
<h3>Leave Controller</h3>
- <b>POST</b> : "/api/v1/member/{memberId}/leave" <br>
<b>Description</b> >> 연차 사용 <br>
<b>Body</b> >> startDay : LocalDate, endDay : LocalDate, reason : String <br><br>
<br>
<h3>WorkRecord Controller</h3>
- <b>GET</b> : "/api/v1/member/{memberId}/attendance-records" <br>
<b>Description</b> >> 직원 근무 기록 조회<br>
<b>Param</b> >> year : int, month : int<br><br>
- <b>GET</b> : "/api/v1/members/over-time" <br>
<b>Description</b> >> 초과 근무 조회 <br>
<b>Param</b> >> year : int, month : int<br><br>
