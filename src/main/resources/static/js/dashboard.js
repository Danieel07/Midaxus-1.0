// ===== Midaxus dashboard.js =====
// Roles, navigation y drag-and-drop en un solo archivo.
// Requiere: auth.js ya cargado (provee getSession / clearSession)

// ─── Definición de roles ──────────────────────────────────────────────────────
const STU_DAYS  = ["Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"];
const STU_SLOTS_DEFAULT = ["07:00-09:00","09:00-11:00","11:00-13:00","13:00-14:00","14:00-16:00","16:00-18:00"];
let STU_SLOTS = [...STU_SLOTS_DEFAULT];
const ROLES = {
  ADMIN: {
    badge: "ADMIN",
    roleTitle: "Administrador General",
    icon: "🛡️",
    theme: "from-red-600 to-red-700",
    menu: [
      { id:"dashboard",   icon:"fas fa-tachometer-alt",  label:"Dashboard"   },
      { id:"courses",     icon:"fas fa-book",            label:"Gestión de Salones y Clases", desc:"Admin de Salones y Clases", emoji:"🏫" },
      { id:"teachers",    icon:"fas fa-chalkboard-user", label:"Gestión de Usuarios", desc:"Administrar estudiantes y profesores", emoji:"👥" },
      { id:"publication", icon:"fas fa-paper-plane",     label:"Publish"     },
      { id:"settings",    icon:"fas fa-cog",             label:"Settings"    }
    ],
    kpis: [
      { label:"Estudiantes Activos", value:"...", icon:"🎓", bg:"bg-blue-100",    dataKey:"totalStudents"   },
      { label:"Profesores",          value:"...", icon:"👨‍🏫", bg:"bg-indigo-100",  dataKey:"totalTeachers"   },
      { label:"Materias",            value:"...", icon:"📄", bg:"bg-green-100",   dataKey:"totalSubjects"   },
      { label:"Aulas",               value:"...", icon:"🏫", bg:"bg-orange-100",  dataKey:"totalRooms"      }
    ],
    quickActions: [
      { label:"Gestión de Usuarios", desc:"Administrar estudiantes y profesores", emoji:"👥", fn: ()=>navigateTo("teachers") },
      { label:"Gestión de Materias", desc:"Catálogo de materias", emoji:"📚", fn: ()=>navigateTo("subjects") },
      { label:"Gestión de Sesiones", desc:"Admin de Salones y Clases", emoji:"🏫", fn: ()=>navigateTo("courses") },
      { label:"Configuración", desc:"Límites de Jornada y Almuerzo", emoji:"⚙️", fn: ()=>navigateTo("settings") }
    ],
    showConflictAlert: true,
    scheduleEditable: true
  },

  TEACHER: {
    badge: "TEACHER",
    roleTitle: "Profesor",
    icon: "👨‍🏫",
    theme: "from-indigo-600 to-indigo-700",
    menu: [
      { id:"dashboard",    icon:"fas fa-tachometer-alt", label:"Dashboard"     },
      { id:"schedule",     icon:"fas fa-calendar-check", label:"Full Schedule" },
      { id:"my-schedule",  icon:"fas fa-user-clock",     label:"My Schedule"   },
      { id:"availability", icon:"fas fa-calendar-times", label:"Availability"  }
    ],
    kpis: [
      { label:"My Courses",   value:"...", icon:"📚", bg:"bg-indigo-100",  dataKey:"myCourses"   },
      { label:"Weekly Hours", value:"...", icon:"🕐", bg:"bg-purple-100",  dataKey:"weeklyHours" },
      { label:"My Groups",    value:"...", icon:"👥", bg:"bg-violet-100",  dataKey:"myGroups"    }
    ],
    quickActions: [
      { label:"Registrar Asistencia", desc:"Gestionar asistencias", emoji:"✏️", fn: ()=>navigateTo("attendance") },
      { label:"Configuración", desc:"Ajustes de perfil", emoji:"⚙️", fn: ()=>toast("Módulo en construcción", "info") }
    ],
    showConflictAlert: false,
    scheduleEditable: false
  },

  STUDENT: {
    badge: "STUDENT",
    roleTitle: "Estudiante",
    icon: "🎓",
    theme: "from-blue-600 to-blue-700",
    menu: [
      { id:"dashboard",          icon:"fas fa-tachometer-alt",  label:"Dashboard"          },
      { id:"enrollment-process", icon:"fas fa-calendar-alt",    label:"Matrícula y Horario" },
      { id:"schedule",           icon:"fas fa-calendar-check",  label:"Horario Completo"   }
    ],
    kpis: [],
    quickActions: [
      { label:"Matrícula y Horario", desc:"Inscribe materias y organiza tu semana", emoji:"📐", fn: ()=>navigateTo("enrollment-process") },
      { label:"Calendario Académico", desc:"Fechas importantes", emoji:"📅", fn: ()=>navigateTo("schedule") }
    ],
    showConflictAlert: false,
    scheduleEditable: false,
    showStudentCourses: true
  }
};

// ─── Datos del horario ────────────────────────────────────────────────────────
const TIME_SLOTS = ["08:00-09:30","09:45-11:15","12:00-13:30","14:00-15:30","16:00-17:30"];
const DAYS       = ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];

let scheduleData = {
  "08:00-09:30":  { Monday:{code:"MAT101",group:"G01",teacher:"Dr. Johnson",room:"301"}, Wednesday:{code:"PHY201",group:"G02",teacher:"Dr. Smith",room:"205"} },
  "09:45-11:15":  { Tuesday:{code:"MAT101",group:"G02",teacher:"Dr. Johnson",room:"301"}, Thursday:{code:"CHEM100",group:"G01",teacher:"Dr. Lewis",room:"114"} },
  "12:00-13:30":  { Friday:{code:"BIO150",group:"G01",teacher:"Dr. Brown",room:"108"} },
  "14:00-15:30":  { Monday:{code:"ENG102",group:"G03",teacher:"Prof. Davis",room:"202"}, Wednesday:{code:"HIST120",group:"G01",teacher:"Dr. Wilson",room:"310"} },
  "16:00-17:30":  {}
};

let pool = [
  { code:"MATH201", group:"G01", teacher:"Dr. Park",    room:"—" },
  { code:"CS101",   group:"G02", teacher:"Prof. White", room:"—" }
];

let dragSrc    = null;
let selectedEl = null;

// Global state variables
let currentScreen = "dashboard";
let autoRefreshTimer = null;
let pendingDeleteAction = null;
var _INTERNAL_USER_REGISTRY_ = []; 
window.adminCoursesData = []; // Global courses cache
window.allTeachersData = [];  // Global teachers cache

// Student Schedule Builder State
let studentScheduleData = {};
let studentPool = [];
let studentDragSrc = null;
let studentPolicies = null;
let studentSubjectsMap = {};
const SUBJECT_EMOJIS = ["📘","📗","📕","📙","📓","📔"];
let LUNCH_SLOT_LABEL = "13:00-14:00";
function decodeJWT(token) {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch(e) {
    return null;
  }
}

const stored = localStorage.getItem("user") || sessionStorage.getItem("user");
let rawAuth = stored ? JSON.parse(stored) : null;
let session = null;

if (rawAuth && rawAuth.token) {
  try {
    const payload = decodeJWT(rawAuth.token);
    if (payload && payload.role) {
      session = {
        id: payload.id || payload.userId || payload.sub || rawAuth.id || payload.email,
        role: (payload.role || "STUDENT").replace("ROLE_", "").toUpperCase(),
        email: payload.email || "",
        name: payload.name || payload.sub || "Usuario",
        initials: payload.name ? payload.name[0].toUpperCase() : "U"
      };
    }
  } catch(e) {
    console.error("Error al decodificar sesión", e);
  }
}

// Inicialización cuando el DOM esté listo
document.addEventListener("DOMContentLoaded", () => {
  if (!session) {
    console.error("Sesión inválida o expirada");
    window.location.href = "/login";
    return;
  }

  const cfg = ROLES[session.role] || ROLES.STUDENT;
  
  // Llenar el perfil en el Banner y Header
  const userNameEl = document.getElementById("user-name");
  if (userNameEl) userNameEl.textContent = session.name || session.email;
  
  const roleSubtitle = document.getElementById("role-subtitle");
  if(roleSubtitle) roleSubtitle.textContent = cfg.roleTitle || cfg.badge;
  
  const profileIcon = document.getElementById("profile-icon");
  if(profileIcon) profileIcon.textContent = cfg.icon || "👤";
  
  const banner = document.getElementById("profile-banner");
  if(banner) banner.className = `text-white rounded-lg shadow-md p-6 mb-6 bg-gradient-to-r ${cfg.theme || 'from-gray-600 to-gray-700'}`;

  const logoutBtn = document.getElementById("btn-logout");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      if (typeof clearSession === "function") clearSession();
      else sessionStorage.clear();
      localStorage.removeItem("user");
      window.location.href = "/";
    });
  }

  buildSidebar(cfg.menu); 
  buildKPIs(cfg.kpis);
  buildQuickActions(cfg.quickActions);
  loadDashboardStats(); 

  const sc = document.getElementById("student-courses");
  if (sc) sc.style.display = session.role === "STUDENT" ? "block" : "none";
  
  const tc = document.getElementById("teacher-courses");
  if (tc) tc.style.display = session.role === "TEACHER" ? "block" : "none";

  buildAvailGrid();
  navigateTo("dashboard");
  startAutoRefresh();
});

// Sesión de demo sin auth.js (cambia el rol para probar)
function demoSession() {
 // return { role:"STUDENT", name:"Demo Student", initials:"DS", email:"demo@midaxus.com" };
 return { role:"TEACHER", name:"Demo Teacher", initials:"DT", email:"demo@midaxus.com" };
  //return { role:"ADMIN", name:"Demo Admin", initials:"DA", email:"demo@midaxus.com" };
}


// ─── Navegación ───────────────────────────────────────────────────────────────

// ─── Custom Confirm Modal Logic ───
function customConfirm(title, message, onConfirm, btnText = "Confirmar", btnClass = "bg-red-600 hover:bg-red-700") {
  const modal = document.getElementById("modal-custom-confirm");
  const t = document.getElementById("confirm-title");
  const m = document.getElementById("confirm-message");
  const b = document.getElementById("confirm-btn-ok");

  if (!modal || !t || !m || !b) {
    if (confirm(`${title}\n\n${message}`)) onConfirm();
    return;
  }

  t.textContent = title;
  m.textContent = message;
  b.textContent = btnText;
  
  // Reset classes and apply new ones
  b.className = `w-full py-3 text-white font-bold rounded-lg shadow-lg transition-all ${btnClass}`;
  
  pendingDeleteAction = onConfirm;
  modal.style.display = "flex";
}

window.closeConfirmModal = () => {
  document.getElementById("modal-custom-confirm").style.display = "none";
  pendingDeleteAction = null;
};

window.executeConfirmAction = () => {
  if (pendingDeleteAction) pendingDeleteAction();
  window.closeConfirmModal();
};

// ─── Auto Refresh Logic ───
function startAutoRefresh() {
  if (autoRefreshTimer) clearInterval(autoRefreshTimer);
  autoRefreshTimer = setInterval(() => {
    if (currentScreen === "dashboard") {
       loadDashboardStats();
       if (session.role === "STUDENT") loadStudentCourses();
       if (session.role === "TEACHER") loadTeacherCourses();
    }
  }, 10000);
}

function navigateTo(id) {
  currentScreen = id;
  document.querySelectorAll(".screen").forEach(s => { s.classList.remove("active"); s.style.display="none"; });
  const tgt = document.getElementById("screen-" + id);
  if (tgt) { tgt.classList.add("active"); tgt.style.display="block"; }

  document.querySelectorAll("#sidebar-menu li").forEach(li => li.classList.toggle("active", li.dataset.id===id));

  if (id === "schedule") {
    const sessionObj = session;
    const editable = ROLES[sessionObj.role]?.scheduleEditable || false;
    buildScheduleUI(editable);
    buildScheduleGrid(editable);
  }
  
  if (id === "courses" && session.role === "ADMIN") {
    loadAdminCourses();
  }
  
  if (id === "subjects" && session.role === "ADMIN") {
    loadAdminSubjects();
  }
  
  if (id === "teachers" && session.role === "ADMIN") {
    loadAdminUsers();
  }
  
  if (id === "settings" && session.role === "ADMIN") {
    loadInstitutionPolicies();
  }
  
  if (id === "dashboard" && session.role === "STUDENT") {
    loadStudentCourses();
  }
  
  if (id === "dashboard" && session.role === "TEACHER") {
    loadTeacherCourses();
  }

  if (id === "attendance" && session.role === "TEACHER") {
    loadTeacherAttendanceClasses();
  }

  if (id === "enrollment-process" && session.role === "STUDENT") {
    initStudentScheduleBuilder();
  }
  
  window.scrollTo(0,0);
}

async function loadAvailableCoursesForUnified() {
  console.log("Cargando materias disponibles para el selector...");
  const select = document.getElementById("unified-enroll-select");
  if (!select) {
    console.warn("No se encontró el elemento unified-enroll-select");
    return;
  }

  let diag = "";
  try {
    const headers = { 'Authorization': 'Bearer ' + rawAuth?.token };
    const [coursesRes, subjectsRes] = await Promise.all([
      fetch('/api/course-groups', { headers, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message })),
      fetch('/api/subjects', { headers, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message }))
    ]);

    let courses = [];
    let allSubj = [];

    if (coursesRes.ok) {
        courses = await coursesRes.json().catch(() => []);
        console.log(`Cursos cargados: ${courses.length}`);
    } else {
        diag += `[Cursos: Error ${coursesRes.status || coursesRes.statusText}] `;
        console.error("Error cargando cursos", coursesRes.statusText);
    }

    if (subjectsRes.ok) {
        allSubj = await subjectsRes.json().catch(() => []);
        console.log(`Materias cargadas: ${allSubj.length}`);
    } else {
        diag += `[Materias: Error ${subjectsRes.status || subjectsRes.statusText}] `;
        console.error("Error cargando materias", subjectsRes.statusText);
    }

    if (!Array.isArray(courses) || courses.length === 0) {
      console.log("No hay cursos disponibles para mostrar en el select");
      select.innerHTML = `<option value="">${diag || 'No hay clases disponibles'}</option>`;
      return;
    }

    select.innerHTML = '<option value="">Seleccione una materia...</option>';
    courses.forEach(cg => {
      if (!cg) return;
      const subj = Array.isArray(allSubj) ? allSubj.find(s => s && s.idSubject === cg.subjectId) : null;
      const subjName = subj ? subj.subjectName : (cg.subjectId || "Materia");
      const opt = document.createElement("option");
      opt.value = cg.courseGroupId;
      const available = (cg.capacity || 0) - (cg.enrolledCount || 0);
      opt.textContent = `${subjName} (Grupo ${cg.code || '—'}) - Disponibles: ${available < 0 ? 0 : available}/${cg.capacity}`;
      select.appendChild(opt);
    });
    
    if (diag !== "") toast(`Aviso: ${diag}`, "warning");

  } catch (err) {
    console.error("Fallo crítico en loadAvailableCoursesForUnified:", err);
    select.innerHTML = `<option value="">Error: ${err.message}</option>`;
  }
}

// ─── LÓGICA DE MATRÍCULA UNIFICADA ───
async function saveEnrollmentUnified() {
  const select = document.getElementById("unified-enroll-select");
  const courseGroupId = select.value;
  if (!courseGroupId) {
    toast("Selecciona una materia para inscribir", "warning");
    return;
  }

  let studentId = session.email || session.id;
  try {
      const sRes = await fetch('/api/students', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
      if (sRes.ok) {
         const students = await sRes.json();
         const me = Array.isArray(students) ? students.find(s => s.email === session.email) : null;
         if (me) {
            studentId = me.studentId || me.id;
         }
      }
  } catch(e) { console.warn("Error resolviendo studentId", e); }

  const dto = {
    studentId: studentId,
    courseGroupId: courseGroupId,
    status: "ENROLLED"
  };

  try {
    const res = await fetch('/api/enrollments', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + rawAuth?.token
      },
      body: JSON.stringify(dto)
    });

    if (res.ok) {
      toast("Materia inscrita correctamente 🎉", "success");
      // Recargar el pool y el selector sin salir de la pantalla
      initStudentScheduleBuilder();
      loadStudentCourses(); // Actualizar también la lista en el dashboard principal
    } else {
      const errText = await res.text();
      let msg = errText;
      try { const obj = JSON.parse(errText); if(obj.message) msg = obj.message; } catch(e){}
      toast("No se pudo inscribir: " + msg, "error");
    }
  } catch (err) {
    toast("Error de red al inscribir", "error");
  }
}

// ─── Institution Policies ──────────────────────────────────────────────
async function loadInstitutionPolicies() {
  try {
    const res = await fetch('/api/policies', {
      headers: { 'Authorization': 'Bearer ' + rawAuth?.token }
    });
    if (res.ok) {
      const data = await res.json();
      if(data.classStartTime) document.getElementById('policy-class-start').value = data.classStartTime.substring(0,5);
      if(data.classEndTime) document.getElementById('policy-class-end').value = data.classEndTime.substring(0,5);
      if(data.lunchStartTime) document.getElementById('policy-lunch-start').value = data.lunchStartTime.substring(0,5);
      if(data.lunchEndTime) document.getElementById('policy-lunch-end').value = data.lunchEndTime.substring(0,5);
      
      if(data.standardCapacity) document.getElementById('policy-standard-capacity').value = data.standardCapacity;
      if(data.capacityTolerancePercent !== undefined) document.getElementById('policy-capacity-tolerance').value = data.capacityTolerancePercent;
      if(data.maxSessionsPerWeek !== undefined) document.getElementById('policy-max-sessions').value = data.maxSessionsPerWeek;
      if(data.minEnrollmentThreshold !== undefined) document.getElementById('policy-min-enrollment').value = data.minEnrollmentThreshold;
      }
      } catch (err) {
      console.error("Error loading policies", err);
      }
      }

      async function saveInstitutionPolicies() {
      const classStart = document.getElementById('policy-class-start').value;
      const classEnd = document.getElementById('policy-class-end').value;
      const lunchStart = document.getElementById('policy-lunch-start').value;
      const lunchEnd = document.getElementById('policy-lunch-end').value;

      const standardCap = document.getElementById('policy-standard-capacity').value;
      const capTolerance = document.getElementById('policy-capacity-tolerance').value;
      const maxSessions = document.getElementById('policy-max-sessions').value;
      const minEnrollment = document.getElementById('policy-min-enrollment').value;

      const payload = {
      classStartTime: classStart ? classStart + ":00" : null,
      classEndTime: classEnd ? classEnd + ":00" : null,
      lunchStartTime: lunchStart ? lunchStart + ":00" : null,
      lunchEndTime: lunchEnd ? lunchEnd + ":00" : null,
      standardCapacity: standardCap ? parseInt(standardCap) : null,
      capacityTolerancePercent: capTolerance ? parseInt(capTolerance) : null,
      maxSessionsPerWeek: maxSessions ? parseInt(maxSessions) : null,
      minEnrollmentThreshold: minEnrollment ? parseInt(minEnrollment) : null
      };

  try {
    const res = await fetch('/api/policies', {
      method: 'PUT',
      headers: { 
        'Authorization': 'Bearer ' + rawAuth?.token,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });
    if (res.ok) {
      toast("Configuración guardada correctamente", "success");
    } else {
      toast("Error al guardar configuración", "error");
    }
  } catch (err) {
    console.error("Error saving policies", err);
    toast("Error de red", "error");
  }
}

// ─── Admin Subjects ───────────────────────────────────────────────────
async function loadAdminSubjects() {
  try {
    const res = await fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
    if (res.ok) {
      const subjects = await res.json();
      const tbody = document.getElementById("admin-subjects-table-body");
      tbody.innerHTML = "";
      
      subjects.forEach(subj => {
        tbody.innerHTML += `
          <tr class="hover:bg-gray-50">
            <td class="p-3 border-b font-mono text-sm">${subj.idSubject}</td>
            <td class="p-3 border-b font-medium text-gray-800">${subj.subjectName}</td>
            <td class="p-3 border-b text-center"><span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full">${subj.sessionPerWeek}</span></td>
            <td class="p-3 border-b text-gray-600">${subj.durationMinutes} min</td>
            <td class="p-3 border-b">
              <div class="flex items-center gap-2">
                <button class="text-indigo-600 hover:text-indigo-800 transition-colors text-sm font-medium" onclick="openEditSubjectModal('${subj.idSubject}','${subj.subjectName.replace(/'/g,"\\'").replace(/"/g,'&quot;')}',${subj.sessionPerWeek})" title="Editar">
                  <i class="fas fa-pen"></i> Editar
                </button>
                <button class="text-red-500 hover:text-red-700 transition-colors text-sm font-medium" onclick="deleteSubject('${subj.idSubject}','${subj.subjectName.replace(/'/g,"\\'").replace(/"/g,'&quot;')}')" title="Eliminar">
                  <i class="fas fa-trash"></i> Eliminar
                </button>
              </div>
            </td>
          </tr>
        `;
      });
    }
  } catch (e) {
    console.error("Error loading subjects", e);
  }
}

function openCreateSubjectModal() {
  document.getElementById("create-subject-id").value = "";
  document.getElementById("create-subject-name").value = "";
  document.getElementById("create-subject-sessions").value = "2";
  document.getElementById("modal-create-subject").style.display = "flex";
}

function closeCreateSubjectModal() {
  document.getElementById("modal-create-subject").style.display = "none";
}

async function createSubject() {
  const idSubject = document.getElementById("create-subject-id").value.trim().toUpperCase();
  const subjectName = document.getElementById("create-subject-name").value.trim();
  const sessionPerWeek = parseInt(document.getElementById("create-subject-sessions").value);
  
  if (!idSubject || !subjectName) {
    toast("El código y el nombre son obligatorios", "error");
    return;
  }
  if (sessionPerWeek < 1 || sessionPerWeek > 3) {
    toast("Las sesiones deben ser entre 1 y 3", "error");
    return;
  }
  
  const payload = {
    idSubject,
    subjectName,
    sessionPerWeek,
    durationMinutes: 120 // Regla de negocio fija
  };
  
  try {
    const res = await fetch('/api/subjects', {
      method: 'POST',
      headers: {
        'Authorization': 'Bearer ' + rawAuth?.token,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });
    
    if (res.ok) {
      toast("Materia creada correctamente", "success");
      closeCreateSubjectModal();
      loadAdminSubjects();
    } else {
      const data = await res.json().catch(()=>null);
      toast(data?.message || "Error al crear la materia", "error");
    }
  } catch (e) {
    console.error(e);
    toast("Error de red", "error");
  }
}

// ─── Edit Subject ─────────────────────────────────────────────────────────────
function openEditSubjectModal(id, name, sessions) {
  document.getElementById("edit-subject-id").value = id;
  document.getElementById("edit-subject-name").value = name;
  document.getElementById("edit-subject-sessions").value = sessions;
  document.getElementById("edit-subject-code-display").textContent = id;
  document.getElementById("modal-edit-subject").style.display = "flex";
}
window.openEditSubjectModal = openEditSubjectModal;

function closeEditSubjectModal() {
  document.getElementById("modal-edit-subject").style.display = "none";
}
window.closeEditSubjectModal = closeEditSubjectModal;

async function saveEditSubject() {
  const id = document.getElementById("edit-subject-id").value;
  const subjectName = document.getElementById("edit-subject-name").value.trim();
  const sessionPerWeek = parseInt(document.getElementById("edit-subject-sessions").value);

  if (!subjectName) {
    toast("El nombre es obligatorio", "error");
    return;
  }
  if (sessionPerWeek < 1 || sessionPerWeek > 3) {
    toast("Las sesiones deben ser entre 1 y 3", "error");
    return;
  }

  try {
    const res = await fetch(`/api/subjects/${id}`, {
      method: 'PUT',
      headers: {
        'Authorization': 'Bearer ' + rawAuth?.token,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ idSubject: id, subjectName, sessionPerWeek, durationMinutes: 120 })
    });

    if (res.ok) {
      toast("Materia actualizada correctamente", "success");
      closeEditSubjectModal();
      loadAdminSubjects();
    } else {
      const data = await res.json().catch(() => null);
      toast(data?.message || "Error al actualizar la materia", "error");
    }
  } catch (e) {
    console.error(e);
    toast("Error de red", "error");
  }
}
window.saveEditSubject = saveEditSubject;

// ─── Delete Subject ───────────────────────────────────────────────────────────
async function deleteSubject(id, name) {
  customConfirm(
    "¿Eliminar materia?",
    `¿Estás seguro de eliminar la materia "${name}" (${id})? Esta acción no se puede deshacer y limpiará dependencias asociadas.`,
    async () => {
      try {
        const res = await fetch(`/api/subjects/${id}`, {
          method: 'DELETE',
          headers: { 'Authorization': 'Bearer ' + rawAuth?.token }
        });

        if (res.ok || res.status === 204) {
          toast(`Materia "${name}" eliminada`, "success");
          loadAdminSubjects();
        } else {
          const data = await res.json().catch(() => null);
          toast(data?.message || "Error al eliminar la materia", "error");
        }
      } catch (e) {
        console.error(e);
        toast("Error de red", "error");
      }
    }
  );
}
window.deleteSubject = deleteSubject;

// ─── Admin Teachers ────────────────────────────────────────────────────
let allSubjectsCache = [];

async function loadAdminTeachers() {
  try {
    const res = await fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
    if (res.ok) {
      const teachers = await res.json();
      const tbody = document.getElementById("admin-teachers-table-body");
      if(!tbody) return;
      tbody.innerHTML = "";
      
      teachers.forEach(t => {
        const comps = t.subjectsIds ? t.subjectsIds.length : 0;
        tbody.innerHTML += `
          <tr class="hover:bg-gray-50">
            <td class="p-3 border-b font-mono text-sm">${t.teacherCode}</td>
            <td class="p-3 border-b font-medium text-gray-800">${t.firstName} ${t.lastName}</td>
            <td class="p-3 border-b"><span class="bg-purple-100 text-purple-800 text-xs px-2 py-1 rounded-full">${comps} materias</span></td>
            <td class="p-3 border-b">
              <button class="text-purple-600 hover:text-purple-800" onclick="openTeacherDetailsModal('${t.teacherCode}', '${t.firstName} ${t.lastName}')">
                <i class="fas fa-edit"></i> Editar Perfil
              </button>
            </td>
          </tr>
        `;
      });
    }
  } catch(e) { console.error(e); }
}

async function openTeacherDetailsModal(uuid, teacherName) {
  document.getElementById("edit-teacher-id").value = uuid;
  document.getElementById("edit-teacher-name").innerText = teacherName;
  
  const matrix = document.getElementById("teacher-avail-matrix");
  const compBox = document.getElementById("teacher-competences-list");
  
  if(matrix) {
    // Reset Matrix with headers (Extremely robust approach)
    matrix.innerHTML = `
      <div class="bg-gray-100 py-2 border-b border-r border-gray-200"></div>
      <div class="bg-gray-100 py-2 border-b border-r border-gray-200 text-[10px] font-bold text-gray-600">LUN</div>
      <div class="bg-gray-100 py-2 border-b border-r border-gray-200 text-[10px] font-bold text-gray-600">MAR</div>
      <div class="bg-gray-100 py-2 border-b border-r border-gray-200 text-[10px] font-bold text-gray-600">MIE</div>
      <div class="bg-gray-100 py-2 border-b border-r border-gray-200 text-[10px] font-bold text-gray-600">JUE</div>
      <div class="bg-gray-100 py-2 border-b border-r border-gray-200 text-[10px] font-bold text-gray-600">VIE</div>
      <div class="bg-gray-100 py-2 border-b border-gray-200 text-[10px] font-bold text-gray-600">SAB</div>
    `;
    
    // Generar filas usando las constantes globales
    STU_SLOTS_DEFAULT.forEach(slot => {
      const timeLabel = document.createElement("div");
      timeLabel.className = "bg-white py-3 border-b border-r border-gray-200 text-[9px] font-bold text-gray-400 flex items-center justify-center";
      timeLabel.textContent = slot;
      matrix.appendChild(timeLabel);
      
      STU_DAYS.forEach(day => {
        const cell = document.createElement("div");
        cell.className = "avail-matrix-cell bg-white border-b border-r border-gray-100 hover:bg-indigo-50 cursor-pointer transition-all h-10";
        cell.dataset.slot = slot;
        cell.dataset.day = day.toUpperCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
        cell.onclick = () => cell.classList.toggle("is-available");
        matrix.appendChild(cell);
      });
    });
  }

  if(compBox) compBox.innerHTML = "<p class='text-sm text-gray-500 animate-pulse text-center py-4 w-full'>Cargando...</p>";
  document.getElementById("modal-teacher-details").style.display = "flex";
  
  try {
    if (allSubjectsCache.length === 0) {
      const sRes = await fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
      if (sRes.ok) allSubjectsCache = await sRes.json();
    }
    
    const tRes = await fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
    const teachers = tRes.ok ? await tRes.json() : [];
    const teacher = teachers.find(t => t.id === uuid || t.teacherCode === uuid);
    
    if (!teacher) {
      toast("No se encontró al docente", "error");
      return;
    }
    
    compBox.innerHTML = "";
    allSubjectsCache.forEach(subj => {
      const isChecked = teacher.subjectsIds && teacher.subjectsIds.includes(subj.idSubject) ? "checked" : "";
      compBox.innerHTML += `
        <label class="flex items-center gap-3 p-3 bg-white border border-gray-200 rounded-xl cursor-pointer hover:border-indigo-400 transition-all">
          <input type="checkbox" class="subj-checkbox w-4 h-4 text-indigo-600 rounded border-gray-300" value="${subj.idSubject}" ${isChecked}>
          <div class="flex flex-col">
            <span class="text-[10px] font-black text-indigo-600 uppercase tracking-tighter">${subj.idSubject}</span>
            <span class="text-xs font-bold text-gray-700 leading-tight">${subj.subjectName}</span>
          </div>
        </label>
      `;
    });
    
    if (teacher.availabilities) {
      teacher.availabilities.forEach(av => {
        const day = av.dayOfWeek.toUpperCase();
        const start = av.startTime.substring(0,5);
        const cell = Array.from(document.querySelectorAll(".avail-matrix-cell")).find(c => 
          c.dataset.day === day && c.dataset.slot.startsWith(start)
        );
        if(cell) cell.classList.add("is-available");
      });
    }
  } catch(e) { console.error(e); }
}

function closeTeacherDetailsModal() {
  document.getElementById("modal-teacher-details").style.display = "none";
}

async function saveTeacherDetails() {
  const uuid = document.getElementById("edit-teacher-id").value;
  const subjectsIds = Array.from(document.querySelectorAll(".subj-checkbox:checked")).map(cb => cb.value);
  
  const availabilities = Array.from(document.querySelectorAll(".avail-matrix-cell.is-available")).map(cell => {
    const [start, end] = cell.dataset.slot.split("-");
    return { dayOfWeek: cell.dataset.day, startTime: start + ":00", endTime: end + ":00" };
  });
  
  try {
    const res = await fetch('/api/teachers/' + uuid, {
      method: 'PUT',
      headers: { 'Authorization': 'Bearer ' + rawAuth?.token, 'Content-Type': 'application/json' },
      body: JSON.stringify({ subjectsIds, availabilities })
    });
    if (res.ok) {
      toast("Perfil actualizado ✓", "success");
      closeTeacherDetailsModal();
      loadAdminUsers();
      // Force reload of teachers cache for enrollment validations
      const tRes = await fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
      if(tRes.ok) allTeachersData = await tRes.json();
    } else {
      toast("Error al actualizar", "error");
    }
  } catch(e) { toast("Error de red", "error"); }
}
window.openTeacherDetailsModal = openTeacherDetailsModal;
window.closeTeacherDetailsModal = closeTeacherDetailsModal;
window.saveTeacherDetails = saveTeacherDetails;

// ─── Admin Courses ───
async function loadAdminCourses() {
  const tbody = document.getElementById("admin-courses-table-body");
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="5" class="p-6 text-center text-blue-600 animate-pulse">Cargando secciones...</td></tr>';

  let diag = "";
  try {
    const [coursesRes, teachersRes, subjectsRes] = await Promise.all([
      fetch('/api/course-groups', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message })),
      fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message })),
      fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message }))
    ]);

    if (coursesRes.ok) {
      try {
        const data = await coursesRes.json();
        adminCoursesData = Array.isArray(data) ? data : [];
      } catch (e) {
        diag += "[Cursos: Error JSON] ";
        adminCoursesData = [];
      }
    } else {
      diag += `[Cursos: Error ${coursesRes.status || coursesRes.statusText}] `;
      adminCoursesData = [];
    }

    if (teachersRes.ok) {
      try {
        const tData = await teachersRes.json();
        allTeachersData = Array.isArray(tData) ? tData : [];
      } catch (e) {
        diag += "[Profesores: Error JSON] ";
        allTeachersData = [];
      }
    } else {
      diag += `[Profesores: Error ${teachersRes.status || teachersRes.statusText}] `;
      allTeachersData = [];
    }

    if (subjectsRes.ok) {
      try {
        const sData = await subjectsRes.json();
        window.allSubjectsList = Array.isArray(sData) ? sData : [];
      } catch (e) {
        diag += "[Materias: Error JSON] ";
        window.allSubjectsList = [];
      }
    } else {
      diag += `[Materias: Error ${subjectsRes.status || subjectsRes.statusText}] `;
      window.allSubjectsList = [];
    }

    if (diag !== "") toast(`Aviso Carga: ${diag}`, "warning");

    renderAdminCourses(diag);
    populateTeacherSelect();
  } catch (err) {
    console.error("Error loading courses or teachers", err);
    tbody.innerHTML = `<tr><td colspan="5" class="p-6 text-center text-red-500 text-xs"><pre>${err.message}\n${err.stack}</pre></td></tr>`;
    toast("Error cargando secciones", "error");
  }
}

function renderAdminCourses(diagInfo = "") {
  const tbody = document.getElementById("admin-courses-table-body");
  if (!tbody) return;
  tbody.innerHTML = "";
  
  if (!Array.isArray(adminCoursesData) || adminCoursesData.length === 0) {
    const msg = diagInfo !== "" ? `Error de carga: ${diagInfo}` : "No hay sesiones registradas.";
    tbody.innerHTML = `<tr><td colspan="5" class="p-6 text-center text-gray-400">${msg}</td></tr>`;
    return;
  }
  
  try {
    adminCoursesData.forEach(cg => {
      // Buscar nombre del profesor para mostrar de forma segura
      let teacherName = "Sin asignar";
      try {
        if (Array.isArray(allTeachersData)) {
          const teacher = allTeachersData.find(t => t.id === cg.teacherId || t.teacherId === cg.teacherId || t.teacherCode === cg.teacherId);
          if (teacher) {
            teacherName = `${teacher.firstName || teacher.userName || "Prof."} ${teacher.lastName || ""}`.trim();
          } else if (cg.teacherId) {
            teacherName = `ID: ${cg.teacherId}`;
          }
        }
      } catch (e) { teacherName = "Error en nombre"; }
      
      // Buscar nombre de la materia de forma segura
      let subjName = cg.subjectId || "Materia";
      try {
        if (Array.isArray(window.allSubjectsList)) {
          const subj = window.allSubjectsList.find(s => s.idSubject === cg.subjectId);
          if (subj) subjName = subj.subjectName;
        }
      } catch (e) { subjName = cg.subjectId || "Materia"; }
      
      const tr = document.createElement("tr");
      tr.className = "hover:bg-gray-50 transition-colors";
      tr.innerHTML = `
        <td class="p-3 border-b font-mono text-xs text-gray-500">${cg.code || "N/A"}</td>
        <td class="p-3 border-b font-medium text-gray-800">${subjName}</td>
        <td class="p-3 border-b text-gray-600">${teacherName}</td>
        <td class="p-3 border-b text-center">
          <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full" title="Inscritos / Capacidad">
            ${cg.enrolledCount || 0} / ${cg.capacity || 0}
          </span>
        </td>
        <td class="p-3 border-b">
          <div class="flex items-center gap-3">
            <button class="text-blue-600 hover:text-blue-800 transition-colors text-sm font-medium flex items-center gap-1" onclick="openEditSessionModal('${cg.courseGroupId}')">
              <i class="fas fa-edit"></i> Asignar
            </button>
            <button class="text-red-500 hover:text-red-700 transition-colors text-sm font-medium flex items-center gap-1" onclick="deleteCourseGroup('${cg.courseGroupId}')">
              <i class="fas fa-trash"></i> Borrar
            </button>
          </div>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (err) {
    console.error("Error renderizando cursos", err);
    tbody.innerHTML = `<tr><td colspan="5" class="p-6 text-center text-red-500 text-xs"><pre>${err.message}\n${err.stack}</pre></td></tr>`;
  }
}

async function deleteCourseGroup(id) {
  customConfirm(
    "¿Eliminar grupo de curso?",
    "Esta acción eliminará permanentemente el grupo, todas sus inscripciones y las sesiones de horario asociadas.",
    async () => {
      try {
        const res = await fetch('/api/course-groups/' + id, {
          method: 'DELETE',
          headers: {
            'Authorization': 'Bearer ' + rawAuth?.token
          }
        });
        
        if (res.ok) {
          toast("Grupo de curso eliminado exitosamente", "success");
          loadAdminCourses();
          if (typeof loadAdminSchedule === "function") loadAdminSchedule();
        } else {
          const errData = await res.json().catch(() => null);
          toast(errData?.message || "Error al eliminar el grupo", "error");
        }
      } catch(err) {
        console.error(err);
        toast("Error de red", "error");
      }
    }
  );
}

function populateTeacherSelect() {
  const select = document.getElementById("edit-session-teacher");
  if (!select) return;
  
  // Limpiar y dejar el default
  select.innerHTML = '<option value="">Seleccione un profesor...</option>';
  
  // Filtrar profesores con contrato activo
  let activeTeachers = allTeachersData.filter(t => t.startDate !== null && t.startDate !== undefined);
  
  // Fallback: Si ningún profesor tiene startDate, mostramos todos.
  if (activeTeachers.length === 0) {
      activeTeachers = allTeachersData;
  }

  activeTeachers.forEach(t => {
    const opt = document.createElement("option");
    // Usar el ID (UUID) como valor principal para asegurar unicidad
    opt.value = t.id || t.teacherCode;
    const fullName = (t.firstName || t.userName || "Prof.") + (t.lastName ? " " + t.lastName : "");
    opt.textContent = `${fullName} (${t.teacherCode || 'Sin código'})`;
    select.appendChild(opt);
  });
}

function openEditSessionModal(courseGroupId) {
  const cg = adminCoursesData.find(c => c.courseGroupId === courseGroupId);
  if (!cg) return;
  
  document.getElementById("edit-session-id").value = cg.courseGroupId;
  document.getElementById("edit-session-teacher").value = cg.teacherId || "";
  document.getElementById("edit-session-capacity").value = cg.capacity || 30;
  
  document.getElementById("modal-edit-session").style.display = "flex";
}

function closeEditSessionModal() {
  document.getElementById("modal-edit-session").style.display = "none";
}

async function saveSessionAssignment() {
  const id = document.getElementById("edit-session-id").value;
  const teacherId = document.getElementById("edit-session-teacher").value;
  const capacity = document.getElementById("edit-session-capacity").value;
  
  if (!teacherId) {
    toast("Debe seleccionar un profesor", "error");
    return;
  }
  
  const cg = adminCoursesData.find(c => c.courseGroupId === id);
  if (!cg) return;
  
  // Actualizar el DTO
  cg.teacherId = teacherId;
  cg.capacity = parseInt(capacity, 10);
  
  try {
    const res = await fetch('/api/course-groups/' + id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + rawAuth?.token
      },
      body: JSON.stringify(cg)
    });
    
    if (res.ok) {
      toast("Sesión asignada correctamente", "success");
      closeEditSessionModal();
      loadAdminCourses(); // Refrescar la tabla
    } else {
      const errData = await res.json().catch(() => null);
      toast(errData?.message || "Error al guardar la sesión", "error");
    }
  } catch(err) {
    console.error(err);
    toast("Error de red", "error");
  }
}

// ─── LÓGICA DE MATRÍCULA DE ESTUDIANTES ───
let enrollableCourses = [];

async function openEnrollModal() {
  const select = document.getElementById("enroll-course-select");
  if (!select) return;
  select.innerHTML = '<option value="">Cargando clases disponibles...</option>';
  document.getElementById("modal-enroll-course").style.display = "flex";

  try {
    const [coursesRes, subjectsRes] = await Promise.all([
      fetch('/api/course-groups', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } }),
      fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } })
    ]);

    if (coursesRes.ok && subjectsRes.ok) {
      enrollableCourses = await coursesRes.json();
      const allSubj = await subjectsRes.json();

      select.innerHTML = '<option value="">Seleccione una clase...</option>';
      enrollableCourses.forEach(cg => {
        // Ignorar llenos (opcional si hubiese enrolledCount)
        const subj = allSubj.find(s => s.idSubject === cg.subjectId || s.idSubject === cg.code);
        const subjName = subj ? subj.subjectName : cg.subjectId;
        const opt = document.createElement("option");
        opt.value = cg.courseGroupId;
        const available = (cg.capacity || 0) - (cg.enrolledCount || 0);
        opt.textContent = `${subjName} (Grupo ${cg.code}) - Disponibles: ${available < 0 ? 0 : available}/${cg.capacity}`;
        select.appendChild(opt);
      });
    } else {
      throw new Error("No se pudo cargar la data");
    }
  } catch (err) {
    console.error(err);
    select.innerHTML = '<option value="">Error cargando opciones</option>';
  }
}

function closeEnrollModal() {
  document.getElementById("modal-enroll-course").style.display = "none";
}

async function saveEnrollment() {
  const courseGroupId = document.getElementById("enroll-course-select").value;
  if (!courseGroupId) {
    toast("Selecciona una materia", "error");
    return;
  }

  let studentId = session.email || session.id; // Infalible
  try {
      const sRes = await fetch('/api/students', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
      if (sRes.ok) {
         const students = await sRes.json();
         const me = students.find(s => s.email === session.email);
         if (me && me.id) studentId = me.id; // Usar siempre el UUID si se encuentra en la BD
         if (me && me.studentId) studentId = me.studentId;
      }
  } catch(e) {
      console.warn("No se pudo obtener listado de students, usando fallback", e);
  }

  const dto = {
    studentId: studentId,
    courseGroupId: courseGroupId,
    status: "ENROLLED"
  };

  try {
    const res = await fetch('/api/enrollments', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + rawAuth?.token
      },
      body: JSON.stringify(dto)
    });

    if (res.ok) {
      toast("Te has matriculado exitosamente 🎉", "success");
      closeEnrollModal();
      loadStudentCourses(); // Refrescar las clases matriculadas
    } else {
      const errText = await res.text();
      alert("Error al matricular: " + errText); // Debug alert
      toast("Error: " + errText, "error");
    }
  } catch (err) {
    toast("Error de red", "error");
  }
}

// ─── CARGAR CLASES INSCRITAS DEL ESTUDIANTE ───
async function loadStudentCourses() {
  const grid = document.getElementById("student-courses-grid");
  if (!grid) return;

  grid.innerHTML = '<p class="text-blue-600 text-center col-span-full py-8 animate-pulse">⏳ Cargando tus clases...</p>';

  // Primero resolver el studentId real del usuario logueado
  let studentId = session.email || session.id;
  let diag = "";
  
  try {
    const sRes = await fetch('/api/students', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' });
    if (sRes.ok) {
      const students = await sRes.json();
      const me = Array.isArray(students) ? students.find(s => s.email === session.email) : null;
      if (me) {
        if (me.studentId) studentId = me.studentId;
        else if (me.id) studentId = me.id;
      } else {
        diag += "[Perfil: No encontrado en lista] ";
      }
    } else {
      diag += `[Perfil: Error ${sRes.status}] `;
    }
  } catch(e) { 
    console.warn("Fallback studentId", e); 
    diag += "[Perfil: Error Red] ";
  }

  try {
    // Cargar cursos matriculados + materias + profesores en paralelo
    const [coursesRes, subjectsRes, teachersRes] = await Promise.all([
      fetch(`/api/enrollments/student/${encodeURIComponent(studentId)}/courses`, { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message })),
      fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message })),
      fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message }))
    ]);

    if (!coursesRes.ok) {
      const errText = coursesRes.statusText || "Error desconocido";
      grid.innerHTML = `<p class="text-gray-400 text-center col-span-full py-8">📭 No se pudieron cargar tus cursos. ${diag} (Error: ${coursesRes.status || errText})</p>`;
      return;
    }

    const courses = await coursesRes.json();
    const subjects = subjectsRes.ok ? await subjectsRes.json() : [];
    const teachers = teachersRes.ok ? await teachersRes.json() : [];

    if (!Array.isArray(courses) || courses.length === 0) {
      grid.innerHTML = `<p class="text-gray-400 text-center col-span-full py-8">📭 No tienes cursos inscritos en este periodo académico. ${diag}</p>`;
      return;
    }

    // Colores para las tarjetas
    const colors = [
      { bg: "bg-blue-50", border: "border-blue-200", badge: "bg-blue-100 text-blue-700", icon: "text-blue-600" },
      { bg: "bg-green-50", border: "border-green-200", badge: "bg-green-100 text-green-700", icon: "text-green-600" },
      { bg: "bg-purple-50", border: "border-purple-200", badge: "bg-purple-100 text-purple-700", icon: "text-purple-600" },
      { bg: "bg-orange-50", border: "border-orange-200", badge: "bg-orange-100 text-orange-700", icon: "text-orange-600" },
      { bg: "bg-pink-50", border: "border-pink-200", badge: "bg-pink-100 text-pink-700", icon: "text-pink-600" },
      { bg: "bg-teal-50", border: "border-teal-200", badge: "bg-teal-100 text-teal-700", icon: "text-teal-600" }
    ];

    const htmlContent = courses.map((cg, i) => {
      const c = colors[i % colors.length];
      const subj = Array.isArray(subjects) ? subjects.find(s => s.idSubject === cg.subjectId) : null;
      const subjName = subj ? subj.subjectName : (cg.subjectId || "Materia");
      const teacher = Array.isArray(teachers) ? teachers.find(t => t.id === cg.teacherId || t.teacherId === cg.teacherId || t.teacherCode === cg.teacherId) : null;
      const teacherName = teacher ? ((teacher.firstName || teacher.userName || "Prof.") + (teacher.lastName ? " " + teacher.lastName : "")) : "Por asignar";

      return `
        <div class="${c.bg} ${c.border} border rounded-lg p-5 hover:shadow-lg transition-all duration-300">
          <div class="flex items-start justify-between mb-3">
            <span class="${c.badge} text-xs font-bold px-2 py-1 rounded-full">Grupo ${cg.code || "—"}</span>
            <span class="bg-green-100 text-green-700 text-xs font-bold px-2 py-1 rounded-full">✅ Inscrito</span>
          </div>
          <h4 class="text-lg font-bold text-gray-800 mb-2">${subjName}</h4>
          <div class="space-y-1 text-sm text-gray-600">
            <p><i class="fas fa-chalkboard-teacher ${c.icon} mr-2"></i>${teacherName}</p>
            <p><i class="fas fa-users ${c.icon} mr-2"></i>Capacidad: ${cg.capacity || "—"}</p>
          </div>
        </div>
      `;
    }).join("");

    grid.innerHTML = htmlContent;

  } catch (err) {
    console.error("Error cargando cursos del estudiante:", err);
    grid.innerHTML = `<p class="text-red-400 text-center col-span-full py-8">❌ Error crítico cargando tus clases: ${err.message}. ${diag}</p>`;
  }
}

// ─── CARGAR CLASES ASIGNADAS DEL DOCENTE ───
async function loadTeacherCourses() {
  const grid = document.getElementById("teacher-courses-grid");
  if (!grid) return;

  grid.innerHTML = '<p class="text-gray-400 text-center col-span-full py-8">⏳ Cargando tus clases...</p>';

  let teacherId = session.id;
  try {
    const tRes = await fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
    if (tRes.ok) {
      const teachers = await tRes.json();
      const me = teachers.find(t => t.email === session.email);
      if (me && me.teacherCode) teacherId = me.teacherCode;
      else if (me && me.id) teacherId = me.id;
    }
  } catch(e) { console.warn("Fallback teacherId", e); }

  try {
    const [coursesRes, subjectsRes] = await Promise.all([
      fetch(`/api/course-groups/teacher/${teacherId}`, { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } }),
      fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } })
    ]);

    if (!coursesRes.ok) {
      grid.innerHTML = '<p class="text-gray-400 text-center col-span-full py-8">📭 No tienes clases asignadas actualmente.</p>';
      return;
    }

    const courses = await coursesRes.json();
    const subjects = subjectsRes.ok ? await subjectsRes.json() : [];

    if (!courses || courses.length === 0) {
      grid.innerHTML = '<p class="text-gray-400 text-center col-span-full py-8">📭 No tienes clases asignadas actualmente.</p>';
      return;
    }

    const htmlContent = courses.map((cg, i) => {
      const subj = subjects.find(s => s.idSubject === cg.subjectId);
      const subjName = subj ? subj.subjectName : (cg.subjectId || "Materia");

      return `
        <div class="bg-indigo-50 border border-indigo-200 rounded-lg p-5 hover:shadow-lg transition-all duration-300">
          <div class="flex items-start justify-between mb-3">
            <span class="bg-indigo-100 text-indigo-700 text-xs font-bold px-2 py-1 rounded-full">Grupo ${cg.code || "—"}</span>
            <span class="bg-indigo-100 text-indigo-600 text-xs font-bold px-2 py-1 rounded-full">👨‍🏫 Asignado</span>
          </div>
          <h4 class="text-lg font-bold text-gray-800 mb-2">${subjName}</h4>
          <div class="space-y-1 text-sm text-gray-600">
            <p><i class="fas fa-users text-indigo-600 mr-2"></i>Cupos: ${cg.capacity || "—"}</p>
          </div>
        </div>
      `;
    }).join("");

    if (window.DOMPurify) {
      grid.innerHTML = DOMPurify.sanitize(htmlContent);
    } else {
      grid.innerHTML = htmlContent;
    }

  } catch (err) {
    console.error("Error cargando cursos del docente:", err);
    grid.innerHTML = `<p class="text-red-400 text-center col-span-full py-8">❌ Error cargando tus clases: ${err.message}.</p>`;
  }
}

async function openCreateScheduleSessionModal() {
  const subjectSelect = document.getElementById("create-session-course");
  const teacherSelect = document.getElementById("create-session-teacher");
  subjectSelect.innerHTML = '<option value="">Cargando materias...</option>';
  teacherSelect.innerHTML = '<option value="">Cargando profesores...</option>';
  document.getElementById("modal-create-schedule-session").style.display = "flex";

  try {
    const [subjectsRes, teachersRes] = await Promise.all([
      fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } }),
      fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } })
    ]);

    const subjects = subjectsRes.ok ? await subjectsRes.json() : [];
    const teachers = teachersRes.ok ? await teachersRes.json() : [];

    subjectSelect.innerHTML = '<option value="">Seleccione una materia...</option>';
    subjects.forEach(s => {
      const opt = document.createElement("option");
      opt.value = s.idSubject;
      opt.textContent = `${s.idSubject} - ${s.subjectName} (${s.sessionPerWeek} ses/sem)`;
      subjectSelect.appendChild(opt);
    });

    teacherSelect.innerHTML = '<option value="">Seleccione un profesor...</option>';
    teachers.forEach(t => {
      const opt = document.createElement("option");
      opt.value = t.teacherCode || t.id;
      opt.textContent = `${(t.firstName || '')} ${(t.lastName || '')} (${t.teacherCode || t.id})`;
      teacherSelect.appendChild(opt);
    });

    if (subjects.length === 0) {
      subjectSelect.innerHTML = '<option value="">No hay materias creadas</option>';
    }
    if (teachers.length === 0) {
      teacherSelect.innerHTML = '<option value="">No hay profesores registrados</option>';
    }
  } catch(e) {
      console.error(e);
      subjectSelect.innerHTML = '<option value="">Error cargando datos</option>';
  }
}

function closeCreateScheduleSessionModal() {
  document.getElementById("modal-create-schedule-session").style.display = "none";
}

async function saveNewScheduleSession() {
  const subjectId = document.getElementById("create-session-course").value;
  const groupCode = document.getElementById("create-session-code").value.trim() || "01";
  const teacherId = document.getElementById("create-session-teacher").value;
  const capacity = parseInt(document.getElementById("create-session-capacity").value) || 30;

  if (!subjectId) {
    toast("Debe seleccionar una materia", "error");
    return;
  }
  if (!teacherId) {
    toast("Debe seleccionar un profesor", "error");
    return;
  }
  if (capacity < 1) {
    toast("La capacidad debe ser mayor a 0", "error");
    return;
  }

  const payload = {
    code: groupCode,
    subjectId: subjectId,
    teacherId: teacherId,
    capacity: capacity
  };

  try {
    const res = await fetch('/api/course-groups', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + rawAuth?.token
      },
      body: JSON.stringify(payload)
    });
      
    if (res.ok) {
      toast("Grupo de curso creado exitosamente", "success");
      closeCreateScheduleSessionModal();
      // Actualización en tiempo real de la tabla de gestión de cursos
      loadAdminCourses();
      // Refresh the schedule screen if it's loaded
      if (typeof loadAdminSchedule === "function") loadAdminSchedule();
    } else {
      const data = await res.json().catch(() => null);
      toast(data?.message || data?.error || "Error al crear el grupo de curso", "error");
    }
  } catch (err) {
    console.error(err);
    toast("Error de red", "error");
  }
}

window.navigateTo = navigateTo;

// ─── Sidebar ──────────────────────────────────────────────────────────────────
function buildSidebar(menu) {
  // Sidebar removida en favor del diseño de Tailwind (Header + Quick Actions Grid)
}

// ─── KPIs ─────────────────────────────────────────────────────────────────────
function buildKPIs(kpis) {
  const g = document.getElementById("kpi-grid");
  if (!g) return;
  
  if (!kpis || kpis.length === 0) {
    g.style.display = 'none';
    return;
  }
  
  g.style.display = 'grid';
  g.innerHTML = kpis.map(k => `
    <div class="bg-white rounded-xl shadow-md p-6 flex items-center gap-4 transition-transform hover:-translate-y-1">
      <div class="${k.bg || 'bg-blue-100 text-blue-600'} p-4 rounded-full w-14 h-14 flex items-center justify-center text-2xl">
        ${k.icon}
      </div>
      <div>
        <p class="text-gray-500 text-sm font-medium">${k.label}</p>
        <p class="text-2xl font-bold text-gray-800" ${k.dataKey ? 'data-kpi="' + k.dataKey + '"' : ''}>${k.value}</p>
      </div>
    </div>`).join("");
}

// ─── Cargar datos reales para los KPIs del dashboard ──────────────────────────
async function loadDashboardStats() {
  try {
    let statsUrl = null;

    if (session.role === "ADMIN") {
      statsUrl = '/api/dashboard/stats/admin';
    } else if (session.role === "TEACHER") {
      // Resolver teacherCode del usuario logueado
      let teacherCode = session.id;
      try {
        const tRes = await fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
        if (tRes.ok) {
          const teachers = await tRes.json();
          const me = teachers.find(t => t.email === session.email);
          if (me && me.teacherCode) teacherCode = me.teacherCode;
        }
      } catch(e) { console.warn("No se pudo resolver teacherCode para stats"); }
      statsUrl = `/api/dashboard/stats/teacher/${teacherCode}`;
    } else if (session.role === "STUDENT") {
      // Resolver studentId del usuario logueado
      let studentId = session.id;
      try {
        const sRes = await fetch('/api/students', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
        if (sRes.ok) {
          const students = await sRes.json();
          const me = students.find(s => s.email === session.email);
          if (me && me.studentId) studentId = me.studentId;
          else if (me && me.id) studentId = me.id;
        }
      } catch(e) { console.warn("No se pudo resolver studentId para stats"); }
      statsUrl = `/api/dashboard/stats/student/${studentId}`;
    }

    if (!statsUrl) return;

    const res = await fetch(statsUrl, {
      headers: { 'Authorization': 'Bearer ' + rawAuth?.token }
    });

    if (!res.ok) {
      console.warn("No se pudieron cargar estadísticas del dashboard", res.status);
      return;
    }

    const data = await res.json();

    // Actualizar cada KPI que tenga un data-kpi attribute
    document.querySelectorAll('[data-kpi]').forEach(el => {
      const key = el.getAttribute('data-kpi');
      if (data[key] !== undefined) {
        const val = data[key];
        // Formatear números grandes con separador de miles
        el.textContent = typeof val === 'number' ? val.toLocaleString() : val;
      }
    });

  } catch (err) {
    console.error("Error cargando estadísticas del dashboard", err);
  }
}

// ─── Quick Actions / Opciones Principales ─────────────────────────────────────
function buildQuickActions(actions) {
  const box = document.getElementById("quick-actions-box");
  if (!box) return;
  box.innerHTML = "";
  
  actions.forEach(a => {
    const btn = document.createElement("button");
    btn.className = "bg-gray-50 hover:bg-gray-100 border border-gray-200 rounded-xl p-4 text-left transition-colors group";
    btn.innerHTML = `
      <div class="flex items-center gap-3 mb-2">
        <span class="text-2xl">${a.emoji || '⚡'}</span>
        <h4 class="font-bold text-gray-800 group-hover:text-blue-600 transition-colors">${a.label}</h4>
      </div>
      <p class="text-sm text-gray-500">${a.desc || ''}</p>
    `;
    btn.addEventListener("click", a.fn);
    box.appendChild(btn);
  });
}

// ─── Availability grid ────────────────────────────────────────────────────────
function buildAvailGrid() {
  const g = document.getElementById("avail-grid");
  if (!g) return;
  const days  = ["Mon","Tue","Wed","Thu","Fri","Sat"];
  const times = ["08:00","09:45","12:00","14:00","16:00"];
  let html = `<div class="avail-time"></div>`;
  days.forEach(d  => html += `<div class="avail-header">${d}</div>`);
  times.forEach(t => {
    html += `<div class="avail-time">${t}</div>`;
    days.forEach(() => html += `<div class="avail-cell" onclick="this.classList.toggle('on')"></div>`);
  });
  g.innerHTML = html;
}

// ─── Schedule UI (toolbar + pool) ─────────────────────────────────────────────
function buildScheduleUI(editable) {
  const sub = document.getElementById("schedule-subtitle");
  if (sub) sub.textContent = editable
      ? "Drag and drop to reschedule · Spring 2026"
      : "View-only · Spring 2026";

  const tb = document.getElementById("schedule-toolbar");
  if (tb) {
    tb.innerHTML = DOMPurify.sanitize(editable
        ? `<button class="btn-sm" onclick="toast('Ready to add class','info')"><i class="fas fa-plus"></i> Add Class</button>
         <button class="btn-sm" onclick="toast('All validated ✓','success')"><i class="fas fa-check-double"></i> Validate</button>
         <button class="btn-sm" onclick="toast('Exporting…','info')"><i class="fas fa-download"></i> Export</button>
         <span style="margin-left:auto;font-size:.78rem;color:var(--muted);">
           <i class="fas fa-hand-paper"></i> Drag cards · drop to pool to unschedule</span>`
        : `<span style="font-size:.82rem;color:var(--muted);">
           <i class="fas fa-eye"></i> View-only – contact admin to request changes</span>`);
  }

  const pz = document.getElementById("pool-zone");
  if (pz) pz.style.display = editable ? "block" : "none";
  if (editable) buildPool();
}

// ─── Schedule Grid ────────────────────────────────────────────────────────────
function buildScheduleGrid(editable) {
  const body = document.getElementById("grid-body");
  if (!body) return;
  body.innerHTML = "";

  // Si no es editable y es estudiante, intentamos cargar su horario desde el backend
  if (!editable && session?.role === "STUDENT") {
    loadStudentScheduleGrid(body);
    return;
  }

  // Comportamiento normal (renderiza scheduleData local)
  renderScheduleGridUI(body, editable, scheduleData);
}

// Nueva función extraída para reusar el renderizado
function renderScheduleGridUI(body, editable, dataToRender) {
  TIME_SLOTS.forEach(slot => {
    const row = document.createElement("div");
    row.className = "grid-row";

    const tc = document.createElement("div");
    tc.className = "time-cell";
    tc.textContent = slot;
    row.appendChild(tc);

    DAYS.forEach(day => {
      const cell = document.createElement("div");
      cell.className = "drop-cell";
      cell.dataset.slot = slot;
      cell.dataset.day  = day;

      if (editable) {
        cell.addEventListener("dragover",  cellDragOver);
        cell.addEventListener("dragleave", cellDragLeave);
        cell.addEventListener("drop",      cellDrop);
      }

      const s = dataToRender[slot]?.[day];
      if (s) cell.appendChild(makeCard(s, slot, day, editable));
      row.appendChild(cell);
    });

    body.appendChild(row);
  });
}

async function loadStudentScheduleGrid(body) {
    body.innerHTML = '<div class="col-span-full py-10 text-center text-gray-500">Cargando horario...</div>';
    
    let studentId = session.email || session.id;
    try {
        const sRes = await fetch('/api/students', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
        if (sRes.ok) {
           const students = await sRes.json();
           const me = students.find(s => s.email === session.email);
           if (me && me.id) studentId = me.id;
           if (me && me.studentId) studentId = me.studentId;
        }
    } catch(e) {}

    try {
        const res = await fetch(`/api/student-schedules/${studentId}`, {
            headers: { 'Authorization': 'Bearer ' + rawAuth?.token }
        });
        
        const apiData = {};
        if (res.ok) {
            const slots = await res.json();
            // Transformar array en mapa {slot: {day: {code, subjectName, group, ...}}}
            slots.forEach(s => {
                if (!apiData[s.slot]) apiData[s.slot] = {};
                // Formatear el día de regreso a Capitalized (ej. "MONDAY" -> "Monday")
                const dayStr = s.day.charAt(0) + s.day.slice(1).toLowerCase();
                apiData[s.slot][dayStr] = {
                    code: s.subjectName || s.courseCode, // Mostrar nombre si es posible
                    group: s.courseCode ? `Grupo ${s.courseCode}` : '',
                    teacher: 'Tu horario', // Podríamos traer el teacherId si lo tuviéramos
                    room: 'Por definir'
                };
            });
        }
        
        body.innerHTML = "";
        renderScheduleGridUI(body, false, apiData);
        
    } catch (err) {
        console.error("Error fetching schedule", err);
        body.innerHTML = '<div class="col-span-full py-10 text-center text-red-500">Error al cargar el horario</div>';
    }
}

function makeCard(s, slot, day, editable) {
  const c = document.createElement("div");
  c.className   = "session-card" + (editable ? "" : " readonly");
  c.dataset.slot = slot;
  c.dataset.day  = day;
  c.innerHTML    = DOMPurify.sanitize(`<strong>${s.code}</strong> ${s.group}
    <span class="sub">${s.teacher}</span>
    <span class="room"><i class="fas fa-map-marker-alt" style="font-size:.6rem;"></i> ${s.room}</span>`);

  if (editable) {
    c.setAttribute("draggable","true");
    c.addEventListener("dragstart", e => {
      dragSrc = { from:"cell", slot, day };
      c.classList.add("dragging");
      e.dataTransfer.effectAllowed = "move";
    });
    c.addEventListener("dragend", () => c.classList.remove("dragging"));
  }
  c.addEventListener("click", e => { e.stopPropagation(); selectCard(c, s, editable); });
  return c;
}

// ─── Pool ─────────────────────────────────────────────────────────────────────
function buildPool() {
  const el = document.getElementById("pool-slots");
  const ct = document.getElementById("pool-count");
  if (!el) return;
  el.innerHTML = "";
  if (ct) ct.textContent = pool.length;

  pool.forEach((item, idx) => {
    const c = document.createElement("div");
    c.className = "pool-card";
    c.setAttribute("draggable","true");
    c.innerHTML = DOMPurify.sanitize(`<i class="fas fa-grip-vertical" style="color:#ccc;margin-right:.3rem;font-size:.7rem;"></i>${item.code} ${item.group}`);
    c.addEventListener("dragstart", e => {
      dragSrc = { from:"pool", idx };
      c.classList.add("dragging");
      e.dataTransfer.effectAllowed = "move";
    });
    c.addEventListener("dragend", () => c.classList.remove("dragging"));
    el.appendChild(c);
  });
}

// Pool drop target — declarado en HTML con ondrop="onPoolDrop(event)"
function onPoolDrop(e) {
  e.preventDefault();
  document.getElementById("pool-zone")?.classList.remove("drag-over");
  if (dragSrc?.from !== "cell") return;

  const s = scheduleData[dragSrc.slot]?.[dragSrc.day];
  if (!s) return;
  pool.push(s);
  delete scheduleData[dragSrc.slot][dragSrc.day];
  dragSrc = null;
  toast(`${s.code} moved to unscheduled pool`, "info");
  buildScheduleGrid(true);
  buildPool();
  hideInspector();
}
window.onPoolDrop = onPoolDrop;

// ─── Cell drag handlers ───────────────────────────────────────────────────────
function cellDragOver(e) {
  e.preventDefault(); e.dataTransfer.dropEffect = "move";
  const cell = e.currentTarget;
  const { slot, day } = cell.dataset;
  const occupied = scheduleData[slot]?.[day];
  const same = dragSrc?.from==="cell" && dragSrc.slot===slot && dragSrc.day===day;
  cell.classList.remove("over","clash");
  if (same) return;
  (occupied && dragSrc?.from==="pool") ? cell.classList.add("clash") : cell.classList.add("over");
}
function cellDragLeave(e) { e.currentTarget.classList.remove("over","clash"); }

function checkConsecutiveDays(courseCode, courseGroup, toDay, fromDay) {
  const toIdx = DAYS.indexOf(toDay);
  if (toIdx === -1) return true;
  
  let scheduledDays = [];
  for (const slot in scheduleData) {
    for (const d in scheduleData[slot]) {
      if (d === fromDay) continue; // skip the origin if moving
      
      const s = scheduleData[slot][d];
      if (s && s.code === courseCode && s.group === courseGroup) {
        if (!scheduledDays.includes(d)) scheduledDays.push(d);
      }
    }
  }
  
  const totalSessions = scheduledDays.length + 1;
  
  // Policy only for 2 or 3 weekly sessions
  if (totalSessions !== 2 && totalSessions !== 3) {
    return true;
  }
  
  let hasAdjacent = false;
  for (const d of scheduledDays) {
    const dIdx = DAYS.indexOf(d);
    if (Math.abs(dIdx - toIdx) === 1) {
      hasAdjacent = true;
      break;
    }
  }
  
  if (hasAdjacent) {
    const msg = `Asignación en días consecutivos detectada para ${courseCode} (${courseGroup}).\n\nPara un aprendizaje óptimo, la política institucional sugiere alternar días para cursos de 2 o 3 sesiones semanales.\n\n¿Deseas registrar la excepción y agendar de todos modos?`;
    if(!confirm(msg)) {
      toast("Asignación cancelada. Selecciona un día alternativo.", "info");
      return false;
    }
  }
  
  return true;
}

async function cellDrop(e) {
  e.preventDefault();
  const cell = e.currentTarget;
  cell.classList.remove("over","clash");
  const { slot:toSlot, day:toDay } = cell.dataset;

  let movedItem = null;

  if (dragSrc?.from === "cell") {
    const { slot:fromSlot, day:fromDay } = dragSrc;
    if (fromSlot===toSlot && fromDay===toDay) { dragSrc=null; return; }
    const s = scheduleData[fromSlot]?.[fromDay];
    if (!s) { dragSrc=null; return; }
    
    // We will let the backend handle the soft warning, but if we have local blocking validations we keep them here.
    // We remove local block for checkConsecutiveDays since backend handles it as Soft Warning.
    
    const existing = scheduleData[toSlot]?.[toDay];
    if (!scheduleData[toSlot]) scheduleData[toSlot] = {};
    scheduleData[toSlot][toDay] = s;
    if (existing) scheduleData[fromSlot][fromDay] = existing;
    else          delete scheduleData[fromSlot][fromDay];
    movedItem = s;
    toast(`${s.code} → ${toDay} ${toSlot}`, "success");

  } else if (dragSrc?.from === "pool") {
    const item = pool[dragSrc.idx];
    if (!item) { dragSrc=null; return; }
    if (scheduleData[toSlot]?.[toDay]) { toast("That cell is occupied","error"); dragSrc=null; return; }
    
    if (!scheduleData[toSlot]) scheduleData[toSlot] = {};
    scheduleData[toSlot][toDay] = { ...item };
    pool.splice(dragSrc.idx, 1);
    movedItem = item;
    toast(`${item.code} scheduled on ${toDay} ${toSlot}`, "success");
  }

  dragSrc = null;
  buildScheduleGrid(true);
  buildPool();
  hideInspector();

  // Call API to save the session and check for soft warnings
  if (movedItem) {
      try {
          const payload = {
              courseGroupId: movedItem.group, // Assuming this maps
              courseCode: movedItem.code,
              day: toDay,
              slot: toSlot,
              teacher: movedItem.teacher
          };
          
          const res = await fetch('/api/schedule-sessions', {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
                  'Authorization': 'Bearer ' + rawAuth?.token
              },
              body: JSON.stringify(payload)
          });
          
          if (res.ok) {
              const data = await res.json();
              if (data.warnings && data.warnings.length > 0) {
                  data.warnings.forEach(w => {
                      // Soft warning alert!
                      toast(w, "warning");
                  });
              }
          }
      } catch (err) {
          console.error("Failed to save schedule session to API", err);
      }
  }
}

// ─── Inspector ────────────────────────────────────────────────────────────────
function selectCard(el, s, editable) {
  if (selectedEl) selectedEl.classList.remove("selected");
  selectedEl = el;
  el.classList.add("selected");

  const panel = document.getElementById("inspector");
  const info  = document.getElementById("inspector-info");
  const acts  = document.getElementById("inspector-actions");
  if (!panel) return;
  panel.style.display = "block";
  info.innerHTML = DOMPurify.sanitize(`<i class="fas fa-info-circle" style="color:var(--teal);margin-right:.4rem;"></i>
    <strong>${s.code}</strong> · ${s.teacher} · Room <strong>${s.room}</strong> · ${el.dataset.day} ${el.dataset.slot}`);
  acts.innerHTML = DOMPurify.sanitize(editable
      ? `<button class="btn-outline" onclick="alert('Edit coming soon')">Edit</button>
       <button class="btn-outline danger" onclick="removeCard('${el.dataset.slot}','${el.dataset.day}')">Remove</button>`
      : "");
}

function hideInspector() {
  if (selectedEl) { selectedEl.classList.remove("selected"); selectedEl = null; }
  const p = document.getElementById("inspector");
  if (p) p.style.display = "none";
}

function removeCard(slot, day) {
  const s = scheduleData[slot]?.[day];
  if (!s) return;
  customConfirm(
    "¿Quitar sesión?",
    `¿Deseas remover "${s.code}" de ${day} a las ${slot}? Se enviará al pool de materias sin asignar.`,
    () => {
      pool.push(s);
      delete scheduleData[slot][day];
      hideInspector();
      toast(`${s.code} removido`, "info");
      buildScheduleGrid(true);
      buildPool();
    }
  );
}
window.removeCard = removeCard;

document.addEventListener("click", e => {
  if (!e.target.closest(".session-card") && !e.target.closest(".inspector")) hideInspector();
});

// ─── STUDENT SCHEDULE BUILDER ─────────────────────────────────────────────────

async function initStudentScheduleBuilder() {
  console.log("--- Iniciando constructor de horario para estudiante ---");
  // Reset state
  studentScheduleData = {};
  studentPool = [];
  studentSubjectsMap = {};

  // Cargar materias disponibles para inscripción inmediatamente
  loadAvailableCoursesForUnified();

  try {
    // 1. Load policies
    try {
      const pRes = await fetch('/api/policies', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' });
      if (pRes.ok) {
        studentPolicies = await pRes.json();
        console.log("Políticas institucionales cargadas");
      }
    } catch(e) { console.warn("Error cargando políticas", e); }

    // 2. Generar slots
    generateSlotsFromPolicies();
    STU_SLOTS.forEach(s => studentScheduleData[s] = {});
    console.log(`Bloques horarios generados: ${STU_SLOTS.length}`);

    // 3. Resolve studentId
    let studentId = session.id;
    console.log(`Resolviendo studentId para sesión: ${session.email || session.id}`);
    try {
      const sRes = await fetch('/api/students', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' });
      if (sRes.ok) {
        const students = await sRes.json();
        const me = Array.isArray(students) ? students.find(s => s.email === session.email) : null;
        if (me) {
          studentId = me.studentId || me.id;
          console.log(`studentId resuelto: ${studentId}`);
        } else {
          console.warn("No se encontró perfil de estudiante coincidente, usando session.id");
        }
      }
    } catch(e) { console.warn("Error resolviendo perfil estudiante", e); }

    // 4. Load data in parallel
    console.log("Solicitando cursos inscritos, materias y profesores...");
    const headers = { 'Authorization': 'Bearer ' + rawAuth?.token };
    const [coursesRes, subjectsRes, teachersRes] = await Promise.all([
      fetch(`/api/enrollments/student/${encodeURIComponent(studentId)}/courses`, { headers, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message })),
      fetch('/api/subjects', { headers, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message })),
      fetch('/api/teachers', { headers, cache: 'no-store' }).catch(e => ({ ok: false, statusText: e.message }))
    ]);

    const subjects = subjectsRes.ok ? await subjectsRes.json().catch(() => []) : [];
    const teachers = teachersRes.ok ? await teachersRes.json().catch(() => []) : [];
    
    // Actualizar cachés globales necesarios para validaciones
    allTeachersData = teachers;
    console.log(`Caché de profesores actualizada: ${allTeachersData.length}`);

    if (Array.isArray(subjects)) {
      subjects.forEach(s => { if(s) studentSubjectsMap[s.idSubject] = s; });
      console.log(`Mapa de materias actualizado: ${Object.keys(studentSubjectsMap).length}`);
    }

    if (coursesRes.ok) {
      const courses = await coursesRes.json().catch(() => []);
      console.log(`Cursos inscritos encontrados: ${courses.length}`);
      if (Array.isArray(courses)) {
        courses.forEach((cg, i) => {
          if (!cg) return;
          const subj = Array.isArray(subjects) ? subjects.find(s => s && s.idSubject === cg.subjectId) : null;
          const teacher = Array.isArray(teachers) ? teachers.find(t => t && (t.id === cg.teacherId || t.teacherCode === cg.teacherId)) : null;
          const sessionsPerWeek = subj ? subj.sessionPerWeek : 2;
          const duration = subj ? subj.durationMinutes : 120;

          for (let s = 0; s < sessionsPerWeek; s++) {
            studentPool.push({
              code: cg.subjectId,
              groupCode: cg.code || "01",
              subjectName: subj ? subj.subjectName : (cg.subjectId || 'Materia'),
              sessionsPerWeek: sessionsPerWeek,
              durationMinutes: duration,
              colorIdx: i % 6,
              courseGroupId: cg.courseGroupId,
              teacherName: teacher ? ((teacher.firstName || '') + ' ' + (teacher.lastName || '')).trim() : 'Por asignar',
              sessionNumber: s + 1,
              teacherId: cg.teacherId
            });
          }
        });
      }
    } else {
      console.error("Error cargando cursos inscritos", coursesRes.statusText);
    }
    
    console.log(`Pool de sesiones pendientes: ${studentPool.length}`);

    // 5. Cargar sesiones ya guardadas
    try {
        const schedRes = await fetch(`/api/student-schedules/${encodeURIComponent(studentId)}`, { headers, cache: 'no-store' });
        if (schedRes.ok) {
            const savedSlots = await schedRes.json();
            console.log(`Sesiones de horario guardadas: ${savedSlots.length}`);
            if (Array.isArray(savedSlots)) {
                savedSlots.forEach(s => {
                    if (!s || !s.day || !s.slot) return;
                    const dayFormatted = s.day.charAt(0) + s.day.slice(1).toLowerCase();
                    const poolIdx = studentPool.findIndex(p => p && p.courseGroupId === s.courseGroupId);
                    if (poolIdx !== -1) {
                        const item = studentPool.splice(poolIdx, 1)[0];
                        if (!studentScheduleData[s.slot]) studentScheduleData[s.slot] = {};
                        studentScheduleData[s.slot][dayFormatted] = item;
                    }
                });
            }
        }
    } catch(e) { console.warn("Error cargando horario guardado", e); }

  } catch (err) {
    console.error("Fallo crítico en initStudentScheduleBuilder:", err);
    toast("Error inicializando el constructor de horario", "error");
  }

  buildStudentPool();
  buildStudentGrid();
  updateStudentProgress();
  validateEntireSchedule();
  console.log("--- Inicialización finalizada ---");
}

window.initStudentScheduleBuilder = initStudentScheduleBuilder;
window.loadAvailableCoursesForUnified = loadAvailableCoursesForUnified;
window.saveEnrollmentUnified = saveEnrollmentUnified;
window.clearStudentSchedule = clearStudentSchedule;
window.saveStudentSchedule = saveStudentSchedule;
window.removeStudentCard = removeStudentCard;

function buildStudentPool() {
  const el = document.getElementById("student-pool-slots");
  const ct = document.getElementById("student-pool-count");
  if (!el) return;
  el.innerHTML = "";
  if (ct) ct.textContent = studentPool.length;

  if (studentPool.length === 0) {
    el.innerHTML = '<p class="text-gray-400 text-sm italic py-2">No tienes materias pendientes de asignar. ¡Inscríbete primero!</p>';
    return;
  }

  studentPool.forEach((item, idx) => {
    const c = document.createElement("div");
    c.className = `student-pool-card color-${item.colorIdx}`;
    c.setAttribute("draggable", "true");
    const durationH = item.durationMinutes ? (item.durationMinutes / 60) : 2;
    c.innerHTML = `
      <span class="pool-emoji">${SUBJECT_EMOJIS[item.colorIdx]}</span>
      <span>${item.subjectName}</span>
      <span class="pool-sessions">${durationH}h</span>
      <span class="pool-sessions" style="background:#6366f1">${item.sessionNumber}/${item.sessionsPerWeek}</span>
    `;
    c.addEventListener("dragstart", e => {
      studentDragSrc = { from: "pool", idx };
      c.classList.add("dragging");
      e.dataTransfer.effectAllowed = "move";
    });
    c.addEventListener("dragend", () => c.classList.remove("dragging"));
    el.appendChild(c);
  });
}

function buildStudentGrid() {
  const body = document.getElementById("student-grid-body");
  if (!body) return;
  body.innerHTML = "";

  // Determine lunch slot
  const lunchSlot = getLunchSlot();

  STU_SLOTS.forEach(slot => {
    const row = document.createElement("div");
    row.className = "student-grid-row";

    const tc = document.createElement("div");
    tc.className = "student-time-cell";
    tc.textContent = slot;
    row.appendChild(tc);

    STU_DAYS.forEach(day => {
      const cell = document.createElement("div");
      cell.className = "student-drop-cell";
      cell.dataset.slot = slot;
      cell.dataset.day = day;

      // Mark lunch block
      if (isLunchSlot(slot)) {
        cell.classList.add("lunch-block");
      } else {
        cell.addEventListener("dragover", studentCellDragOver);
        cell.addEventListener("dragleave", studentCellDragLeave);
        cell.addEventListener("drop", studentCellDrop);
      }

      // Render existing card
      const s = studentScheduleData[slot]?.[day];
      if (s) cell.appendChild(makeStudentCard(s, slot, day));
      row.appendChild(cell);
    });

    body.appendChild(row);
  });
}

function makeStudentCard(s, slot, day) {
  const c = document.createElement("div");
  c.className = `student-schedule-card color-${s.colorIdx}`;
  c.dataset.slot = slot;
  c.dataset.day = day;
  c.setAttribute("draggable", "true");
  const durationH = s.durationMinutes ? (s.durationMinutes / 60) : 2;
  c.innerHTML = DOMPurify.sanitize(`
    <div>${SUBJECT_EMOJIS[s.colorIdx]} <strong>${s.code}</strong></div>
    <span class="card-subject">${s.subjectName}</span>
    <span class="card-subject" style="color:#6366f1;font-weight:600;"><i class="fas fa-clock" style="font-size:.6rem"></i> ${durationH}h &middot; ${s.teacherName || ''}</span>
    <span class="card-remove" onclick="removeStudentCard('${slot}','${day}')" title="Quitar"><i class="fas fa-times"></i></span>
  `);
  c.addEventListener("dragstart", e => {
    studentDragSrc = { from: "cell", slot, day };
    c.classList.add("dragging");
    e.dataTransfer.effectAllowed = "move";
  });
  c.addEventListener("dragend", () => c.classList.remove("dragging"));
  return c;
}

function studentCellDragOver(e) {
  e.preventDefault();
  e.dataTransfer.dropEffect = "move";
  const cell = e.currentTarget;
  const { slot, day } = cell.dataset;

  if (isLunchSlot(slot)) return;

  const occupied = studentScheduleData[slot]?.[day];
  const sameCell = studentDragSrc?.from === "cell" && studentDragSrc.slot === slot && studentDragSrc.day === day;
  cell.classList.remove("over", "clash");
  if (sameCell) return;

  if (occupied && studentDragSrc?.from === "pool") {
    cell.classList.add("clash");
  } else {
    cell.classList.add("over");
  }
}

function studentCellDragLeave(e) {
  e.currentTarget.classList.remove("over", "clash");
}

function studentCellDrop(e) {
  e.preventDefault();
  const cell = e.currentTarget;
  cell.classList.remove("over", "clash");
  const { slot: toSlot, day: toDay } = cell.dataset;

  if (isLunchSlot(toSlot)) {
    toast("No puedes colocar clases en el bloque de almuerzo", "error");
    studentDragSrc = null;
    return;
  }

  let itemToPlace = null;
  let fromSlot = null, fromDay = null;

  if (studentDragSrc?.from === "pool") {
    itemToPlace = studentPool[studentDragSrc.idx];
    if (!itemToPlace) { studentDragSrc = null; return; }

    // Check cell occupied
    if (studentScheduleData[toSlot]?.[toDay]) {
      toast("Ese bloque ya tiene una materia asignada", "error");
      studentDragSrc = null;
      return;
    }
  } else if (studentDragSrc?.from === "cell") {
    fromSlot = studentDragSrc.slot;
    fromDay = studentDragSrc.day;
    if (fromSlot === toSlot && fromDay === toDay) { studentDragSrc = null; return; }
    itemToPlace = studentScheduleData[fromSlot]?.[fromDay];
    if (!itemToPlace) { studentDragSrc = null; return; }
  }

  // ── VALIDATE POLICIES ──
  const validation = validateStudentPlacement(itemToPlace, toDay, toSlot, fromDay);
  if (!validation.valid) {
    toast(validation.message, "error");
    studentDragSrc = null;
    return;
  }
  if (validation.warning) {
    toast(validation.warning, "warning");
  }

  // ── PLACE ──
  if (studentDragSrc?.from === "pool") {
    if (!studentScheduleData[toSlot]) studentScheduleData[toSlot] = {};
    studentScheduleData[toSlot][toDay] = { ...itemToPlace };
    studentPool.splice(studentDragSrc.idx, 1);
    toast(`${itemToPlace.subjectName} → ${toDay} ${toSlot}`, "success");
  } else if (studentDragSrc?.from === "cell") {
    const existing = studentScheduleData[toSlot]?.[toDay];
    if (!studentScheduleData[toSlot]) studentScheduleData[toSlot] = {};
    studentScheduleData[toSlot][toDay] = itemToPlace;
    if (existing) {
      studentScheduleData[fromSlot][fromDay] = existing;
    } else {
      delete studentScheduleData[fromSlot][fromDay];
    }
    toast(`${itemToPlace.subjectName} → ${toDay} ${toSlot}`, "success");
  }

  studentDragSrc = null;
  buildStudentPool();
  buildStudentGrid();
  updateStudentProgress();
  validateEntireSchedule();
}

function onStudentPoolDrop(e) {
  e.preventDefault();
  document.getElementById("student-pool-zone")?.classList.remove("border-blue-500", "bg-blue-100/50");
  if (studentDragSrc?.from !== "cell") return;

  const s = studentScheduleData[studentDragSrc.slot]?.[studentDragSrc.day];
  if (!s) return;

  studentPool.push(s);
  delete studentScheduleData[studentDragSrc.slot][studentDragSrc.day];
  studentDragSrc = null;
  toast(`${s.subjectName} devuelta al pool`, "info");
  buildStudentPool();
  buildStudentGrid();
  updateStudentProgress();
  validateEntireSchedule();
}
window.onStudentPoolDrop = onStudentPoolDrop;

function removeStudentCard(slot, day) {
  const s = studentScheduleData[slot]?.[day];
  if (!s) return;
  studentPool.push(s);
  delete studentScheduleData[slot][day];
  toast(`${s.subjectName} removida del horario`, "info");
  buildStudentPool();
  buildStudentGrid();
  updateStudentProgress();
  validateEntireSchedule();
}
window.removeStudentCard = removeStudentCard;

// ── VALIDATION ──

function validateStudentPlacement(item, toDay, toSlot, fromDay) {
  // 1. Almuerzo
  if (isLunchSlot(toSlot)) return { valid: false, message: "Bloque de almuerzo no disponible" };

  // 2. Disponibilidad del Profesor (ESTRICTO)
  const teacher = allTeachersData.find(t => t.id === item.teacherId || t.teacherCode === item.teacherId);
  if (teacher && teacher.availabilities) {
    const dayUpper = toDay.toUpperCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
    const [slotStart] = toSlot.split("-");
    
    const isAvailable = teacher.availabilities.some(av => {
      return av.dayOfWeek.toUpperCase() === dayUpper && 
             av.startTime.substring(0,5) === slotStart;
    });

    if (!isAvailable) {
      return { 
        valid: false, 
        message: `El profesor ${item.teacherName || 'asignado'} no tiene disponibilidad el ${toDay} a las ${toSlot}.` 
      };
    }
  }

  // 3. Días consecutivos
  const toDayIdx = STU_DAYS.indexOf(toDay);
  const scheduledDays = [];
  for (const sl in studentScheduleData) {
    for (const d in studentScheduleData[sl]) {
      if (d === fromDay) continue;
      const entry = studentScheduleData[sl][d];
      if (entry && entry.code === item.code && !scheduledDays.includes(d)) scheduledDays.push(d);
    }
  }
  for (const d of scheduledDays) {
    const dIdx = STU_DAYS.indexOf(d);
    if (Math.abs(dIdx - toDayIdx) === 1) {
      return { valid: false, message: `Días consecutivos no permitidos para ${item.subjectName}` };
    }
  }

  // 4. Misma materia mismo día
  for (const sl in studentScheduleData) {
    if (sl === toSlot) continue;
    const entry = studentScheduleData[sl]?.[toDay];
    if (entry && entry.code === item.code) {
        if (fromDay === toDay && studentScheduleData[studentDragSrc?.slot]?.[studentDragSrc?.day]?.code === item.code) continue;
        return { valid: false, message: `"${item.subjectName}" ya está asignada este día` };
    }
  }

  // 5. Límite sesiones semanales
  if (studentPolicies?.maxSessionsPerWeek) {
    let totalSessions = scheduledDays.length + 1;
    if (totalSessions > studentPolicies.maxSessionsPerWeek) {
      return { valid: false, message: `Límite de ${studentPolicies.maxSessionsPerWeek} sesiones semanales superado` };
    }
  }

  return { valid: true };
}

function isLunchSlot(slot) {
  return slot === LUNCH_SLOT_LABEL;
}

function getLunchSlot() {
  return LUNCH_SLOT_LABEL;
}

/**
 * Genera los bloques de tiempo dinámicamente a partir de las políticas.
 * Bloques de 2 horas (120 min) para clases, con el almuerzo insertado como bloque aparte.
 */
function generateSlotsFromPolicies() {
  if (!studentPolicies) return; // usar defaults

  const pad = n => String(n).padStart(2, '0');
  const toMin = timeStr => {
    if (!timeStr) return null;
    const parts = timeStr.split(':');
    return parseInt(parts[0]) * 60 + parseInt(parts[1]);
  };
  const toLabel = min => `${pad(Math.floor(min/60))}:${pad(min%60)}`;

  const classStart = toMin(studentPolicies.classStartTime) ?? 420;  // 07:00
  const classEnd   = toMin(studentPolicies.classEndTime)   ?? 1080; // 18:00
  const lunchStart = toMin(studentPolicies.lunchStartTime) ?? 780;  // 13:00
  const lunchEnd   = toMin(studentPolicies.lunchEndTime)   ?? 840;  // 14:00
  const sessionLen = 120; // minutos por sesión de clase

  LUNCH_SLOT_LABEL = `${toLabel(lunchStart)}-${toLabel(lunchEnd)}`;
  const slots = [];
  let cursor = classStart;

  while (cursor < classEnd) {
    // Si el cursor está justo al inicio del almuerzo, insertar bloque de almuerzo
    if (cursor === lunchStart && lunchStart < lunchEnd) {
      slots.push(`${toLabel(lunchStart)}-${toLabel(lunchEnd)}`);
      cursor = lunchEnd;
      continue;
    }

    // Si el siguiente bloque de clase caería dentro del almuerzo, cortarlo antes
    let blockEnd = cursor + sessionLen;
    if (cursor < lunchStart && blockEnd > lunchStart) {
      // El bloque termina justo antes del almuerzo
      blockEnd = lunchStart;
    }

    // No exceder el fin de jornada
    if (blockEnd > classEnd) blockEnd = classEnd;

    // Agregar bloque solo si tiene duración y asegura el avance del cursor
    if (blockEnd > cursor) {
      slots.push(`${toLabel(cursor)}-${toLabel(blockEnd)}`);
      cursor = blockEnd;
    } else {
      // Fallback para evitar bucle infinito si por alguna razón blockEnd <= cursor
      cursor += 1; 
    }
  }

  STU_SLOTS = slots;
}

function validateEntireSchedule() {
  const msgBox = document.getElementById("student-schedule-messages");
  if (!msgBox) return;
  msgBox.innerHTML = "";

  const messages = [];

  // Check each subject has enough sessions
  const subjectSessions = {};
  const subjectRequired = {};
  
  // Count assigned sessions
  for (const slot in studentScheduleData) {
    for (const day in studentScheduleData[slot]) {
      const entry = studentScheduleData[slot][day];
      if (entry) {
        subjectSessions[entry.code] = (subjectSessions[entry.code] || 0) + 1;
        subjectRequired[entry.code] = entry.sessionsPerWeek;
      }
    }
  }

  // Also count pool items
  studentPool.forEach(p => {
    subjectRequired[p.code] = p.sessionsPerWeek;
  });

  // Check completeness
  for (const code in subjectRequired) {
    const assigned = subjectSessions[code] || 0;
    const needed = subjectRequired[code];
    if (assigned < needed) {
      messages.push({
        type: "warning",
        icon: "fas fa-exclamation-triangle",
        text: `"${code}" necesita ${needed} sesiones, solo tiene ${assigned} asignadas`
      });
    } else if (assigned === needed) {
      messages.push({
        type: "success",
        icon: "fas fa-check-circle",
        text: `"${code}" completó sus ${needed} sesiones semanales ✓`
      });
    }
  }

  if (studentPool.length === 0 && messages.every(m => m.type === "success")) {
    messages.unshift({
      type: "success",
      icon: "fas fa-star",
      text: "¡Horario completo! Todas las materias tienen sus sesiones asignadas. Guarda tu horario."
    });
  }

  msgBox.innerHTML = messages.map(m => `
    <div class="schedule-msg ${m.type}">
      <i class="${m.icon}"></i>
      <span>${m.text}</span>
    </div>
  `).join("");
}

function updateStudentProgress() {
  const bar = document.getElementById("schedule-progress-bar");
  const text = document.getElementById("schedule-progress-text");
  if (!bar || !text) return;

  let totalNeeded = 0;
  let totalAssigned = 0;

  // Count assigned
  for (const slot in studentScheduleData) {
    for (const day in studentScheduleData[slot]) {
      if (studentScheduleData[slot][day]) totalAssigned++;
    }
  }
  totalNeeded = totalAssigned + studentPool.length;

  const pct = totalNeeded > 0 ? Math.round((totalAssigned / totalNeeded) * 100) : 0;
  bar.style.width = pct + "%";
  text.textContent = `${totalAssigned} / ${totalNeeded} sesiones asignadas`;

  // Color based on progress
  if (pct === 100) {
    bar.className = "bg-gradient-to-r from-green-400 to-emerald-500 h-2.5 rounded-full transition-all duration-500";
  } else if (pct > 50) {
    bar.className = "bg-gradient-to-r from-blue-500 to-indigo-500 h-2.5 rounded-full transition-all duration-500";
  } else {
    bar.className = "bg-gradient-to-r from-amber-400 to-orange-500 h-2.5 rounded-full transition-all duration-500";
  }
}

function clearStudentSchedule() {
  customConfirm(
    "¿Limpiar horario?",
    "¿Estás seguro de que deseas quitar todas las materias asignadas de tu horario actual?",
    () => {
      for (const slot in studentScheduleData) {
        for (const day in studentScheduleData[slot]) {
          if (studentScheduleData[slot][day]) {
            studentPool.push(studentScheduleData[slot][day]);
            delete studentScheduleData[slot][day];
          }
        }
      }
      buildStudentPool();
      buildStudentGrid();
      updateStudentProgress();
      validateEntireSchedule();
      toast("Horario limpiado", "info");
    }
  );
}
window.clearStudentSchedule = clearStudentSchedule;

async function saveStudentSchedule() {
  const processSave = async () => {
    const entries = [];
    for (const slot in studentScheduleData) {
      for (const day in studentScheduleData[slot]) {
        const entry = studentScheduleData[slot][day];
        if (entry) {
          const dayEnum = day.toUpperCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
          entries.push({
            courseGroupId: entry.courseGroupId,
            courseCode: entry.code,
            day: dayEnum,
            slot: slot,
            subjectName: entry.subjectName
          });
        }
      }
    }

    if (entries.length === 0) {
      toast("No hay sesiones para guardar", "error");
      return;
    }

    let studentId = session.email || session.id;
    try {
        const sRes = await fetch('/api/students', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
        if (sRes.ok) {
           const students = await sRes.json();
           const me = students.find(s => s.email === session.email);
           if (me && me.id) studentId = me.id;
           if (me && me.studentId) studentId = me.studentId;
        }
    } catch(e) {}

    try {
        const res = await fetch(`/api/student-schedules/${studentId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + rawAuth?.token
            },
            body: JSON.stringify(entries)
        });
        
        if (res.ok) {
            toast(`Horario guardado con ${entries.length} sesiones ✓`, "success");
        } else {
            toast("Error al guardar el horario", "error");
        }
    } catch (err) {
        toast("Error de red", "error");
    }
  };

  if (studentPool.length > 0) {
    customConfirm(
      "Horario incompleto",
      "Aún tienes materias sin asignar en tu pool. ¿Deseas guardar el horario parcial?",
      processSave
    );
  } else {
    processSave();
  }
}
window.saveStudentSchedule = saveStudentSchedule;

// ─── Toast ────────────────────────────────────────────────────────────────────
function toast(msg, type="info") {
  const root = document.getElementById("toast-root");
  if (!root) return;
  const t = document.createElement("div");
  t.className = "toast " + type;
  if (type === "warning") {
      t.style.backgroundColor = "#fff3cd";
      t.style.color = "#856404";
      t.style.borderLeft = "4px solid #ffeeba";
  }
  const icons = { success:"fa-check-circle", error:"fa-exclamation-circle", info:"fa-info-circle", warning:"fa-exclamation-triangle" };
  t.innerHTML = `<i class="fas ${icons[type]||icons.info}"></i> ${msg}`;
  root.appendChild(t);
  setTimeout(() => { t.style.transition=".3s"; t.style.opacity="0"; t.style.transform="translateX(28px)"; }, 3000);
  setTimeout(() => t.remove(), 3400);
}
window.toast = toast;

// ─── ATTENDANCE LOGIC (HU-18) ──────────────────────────────────────────────────
async function loadTeacherAttendanceClasses() {
  const list = document.getElementById("attendance-classes-list");
  if (!list) return;
  list.innerHTML = '<p class="text-indigo-600 animate-pulse text-sm">Cargando tus clases...</p>';
  
  // Hide students container and show empty state
  const stuContainer = document.getElementById("attendance-students-container");
  const emptyState = document.getElementById("attendance-empty-state");
  if (stuContainer) stuContainer.style.display = "none";
  if (emptyState) emptyState.style.display = "flex";

  let teacherId = session.id;
  try {
    const tRes = await fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } });
    if (tRes.ok) {
      const teachers = await tRes.json();
      const me = teachers.find(t => t.email === session.email);
      if (me && (me.teacherCode || me.id)) teacherId = me.teacherCode || me.id;
    }
  } catch(e) { console.warn("Fallback teacherId", e); }

  try {
    const [coursesRes, subjectsRes] = await Promise.all([
      fetch(`/api/course-groups/teacher/${teacherId}`, { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } }),
      fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } })
    ]);

    if (!coursesRes.ok) throw new Error("Error loading courses");
    const courses = await coursesRes.json();
    const subjects = subjectsRes.ok ? await subjectsRes.json() : [];

    if (courses.length === 0) {
      list.innerHTML = '<p class="text-gray-400 text-sm italic py-4">No tienes clases asignadas.</p>';
      return;
    }

    list.innerHTML = "";
    courses.forEach(cg => {
      const subj = subjects.find(s => s.idSubject === cg.subjectId);
      const subjName = subj ? subj.subjectName : (cg.subjectId || "Materia");
      
      const btn = document.createElement("button");
      btn.className = "w-full text-left p-3 rounded-lg border border-gray-100 hover:border-indigo-300 hover:bg-indigo-50 transition-all group";
      btn.innerHTML = `
        <div class="font-bold text-gray-800 group-hover:text-indigo-700">${subjName}</div>
        <div class="text-xs text-gray-500">Grupo ${cg.code} · ${cg.capacity} cupos</div>
      `;
      btn.onclick = () => loadClassStudentsForAttendance(cg.courseGroupId, subjName, cg.code);
      list.appendChild(btn);
    });

  } catch (err) {
    console.error(err);
    list.innerHTML = '<p class="text-red-500 text-sm">Error al cargar clases.</p>';
  }
}

let currentAttendanceMap = new Map();
let currentSelectedCourseGroupId = null;

async function loadClassStudentsForAttendance(courseGroupId, subjectName, groupCode) {
  currentSelectedCourseGroupId = courseGroupId;
  currentAttendanceMap.clear();

  const container = document.getElementById("attendance-students-container");
  const emptyState = document.getElementById("attendance-empty-state");
  const tableBody = document.getElementById("attendance-students-table-body");
  const title = document.getElementById("attendance-selected-class-title");
  const dateDisplay = document.getElementById("attendance-date-display");

  if (!container || !tableBody) return;

  title.textContent = `${subjectName} - Grupo ${groupCode}`;
  const today = new Date();
  const dateStr = today.toISOString().split('T')[0];
  dateDisplay.textContent = today.toLocaleDateString('es-ES', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
  
  tableBody.innerHTML = '<tr><td colspan="3" class="p-6 text-center text-indigo-600 animate-pulse">Cargando lista de estudiantes...</td></tr>';
  if (emptyState) emptyState.style.display = "none";
  container.style.display = "block";

  try {
    const [studentsRes, attendanceRes] = await Promise.all([
      fetch(`/api/enrollments/course/${courseGroupId}/students`, {
        headers: { 'Authorization': 'Bearer ' + rawAuth?.token }
      }),
      fetch(`/api/attendance/course/${courseGroupId}?date=${dateStr}`, {
        headers: { 'Authorization': 'Bearer ' + rawAuth?.token }
      })
    ]);

    if (!studentsRes.ok) throw new Error("Error loading students");
    const students = await studentsRes.json();
    const existingAttendance = attendanceRes.ok ? await attendanceRes.json() : [];

    // Map existing attendance
    existingAttendance.forEach(a => {
        currentAttendanceMap.set(a.studentId, a.present);
    });

    if (students.length === 0) {
      tableBody.innerHTML = '<tr><td colspan="3" class="p-10 text-center text-gray-400 italic">No hay estudiantes inscritos en este grupo.</td></tr>';
      return;
    }

    tableBody.innerHTML = students.map(s => {
      const isPresent = currentAttendanceMap.get(s.studentId);
      let statusText = "Sin marcar";
      let statusClass = "bg-gray-100 text-gray-400";
      
      if (isPresent === true) {
          statusText = "Asistencia";
          statusClass = "bg-green-100 text-green-700";
      } else if (isPresent === false) {
          statusText = "Inasistencia";
          statusClass = "bg-red-100 text-red-700";
      }

      return `
      <tr class="hover:bg-gray-50 transition-colors" id="row-stu-${s.studentId}">
        <td class="p-3">
          <div class="font-medium text-gray-800">${s.firstName} ${s.lastName}</div>
          <div class="text-xs text-gray-400 font-mono">${s.studentId}</div>
        </td>
        <td class="p-3 text-center">
          <span id="status-stu-${s.studentId}" class="text-xs font-bold px-2 py-1 rounded-full ${statusClass}">${statusText}</span>
        </td>
        <td class="p-3">
          <div class="flex justify-end gap-2">
            <button onclick="markStudentAttendance('${s.studentId}', true)" 
                    class="w-10 h-10 rounded-lg flex items-center justify-center border border-gray-200 hover:border-green-500 hover:bg-green-50 text-green-600 transition-all shadow-sm" title="Presente">
              ✅
            </button>
            <button onclick="markStudentAttendance('${s.studentId}', false)" 
                    class="w-10 h-10 rounded-lg flex items-center justify-center border border-gray-200 hover:border-red-500 hover:bg-red-50 text-red-600 transition-all shadow-sm" title="Ausente">
              ❌
            </button>
          </div>
        </td>
      </tr>
    `;}).join("");

  } catch (err) {
    console.error(err);
    tableBody.innerHTML = '<tr><td colspan="3" class="p-6 text-center text-red-500">Error al cargar estudiantes.</td></tr>';
  }
}

function markStudentAttendance(studentId, isPresent) {
  const statusBadge = document.getElementById(`status-stu-${studentId}`);
  if (!statusBadge) return;

  currentAttendanceMap.set(studentId, isPresent);

  if (isPresent) {
    statusBadge.textContent = "Asistencia";
    statusBadge.className = "text-xs font-bold px-2 py-1 rounded-full bg-green-100 text-green-700";
  } else {
    statusBadge.textContent = "Inasistencia";
    statusBadge.className = "text-xs font-bold px-2 py-1 rounded-full bg-red-100 text-red-700";
  }
}

async function saveCurrentAttendance() {
    if (!currentSelectedCourseGroupId) return;
    if (currentAttendanceMap.size === 0) {
        toast("No hay cambios para guardar", "info");
        return;
    }

    const today = new Date().toISOString().split('T')[0];
    const payload = Array.from(currentAttendanceMap.entries()).map(([studentId, present]) => ({
        studentId,
        courseGroupId: currentSelectedCourseGroupId,
        date: today,
        present
    }));

    try {
        const res = await fetch('/api/attendance/batch', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + rawAuth?.token
            },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            toast("Asistencia guardada permanentemente ✅", "success");
        } else {
            toast("Error al guardar asistencia", "error");
        }
    } catch (err) {
        console.error(err);
        toast("Error de red", "error");
    }
}
window.saveCurrentAttendance = saveCurrentAttendance;

window.loadTeacherAttendanceClasses = loadTeacherAttendanceClasses;
window.loadClassStudentsForAttendance = loadClassStudentsForAttendance;
window.markStudentAttendance = markStudentAttendance;

// ─── UNIFIED USER MANAGEMENT ───
async function loadAdminUsers() {
  const tbody = document.getElementById("admin-users-table-body");
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="6" class="p-6 text-center text-purple-600 animate-pulse">Cargando usuarios...</td></tr>';

  let teachers = [];
  let students = [];
  let diag = "";

  // 1. Fetch Profesores de forma aislada
  try {
    const tRes = await fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' });
    if (tRes.ok) {
      const data = await tRes.json();
      teachers = Array.isArray(data) ? data : [];
    } else {
      diag += `[Profesores: Error ${tRes.status}] `;
    }
  } catch (e) { 
    console.error(e); 
    diag += `[Profesores: Error Red] `;
  }

  // 2. Fetch Estudiantes de forma aislada
  try {
    const sRes = await fetch('/api/students', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token }, cache: 'no-store' });
    if (sRes.ok) {
      const data = await sRes.json();
      students = Array.isArray(data) ? data : [];
    } else {
      diag += `[Estudiantes: Error ${sRes.status}] `;
    }
  } catch (e) { 
    console.error(e); 
    diag += `[Estudiantes: Error Red] `;
  }

  try {
    // Consolidar lista de forma ultra-segura
    const safeMap = (raw, role, rawRole) => {
      try {
        if (!raw) return null;
        return { 
          id: String(raw.teacherCode || raw.studentId || raw.id || "N/A"), 
          name: `${raw.firstName || ''} ${raw.lastName || ''}`.trim() || raw.userName || "Sin Nombre", 
          email: String(raw.email || "S/C"), 
          role: role, 
          rawRole: rawRole, 
          uuid: String(raw.id || '') 
        };
      } catch (e) { return null; }
    };

    _INTERNAL_USER_REGISTRY_ = [
      ...(Array.isArray(teachers) ? teachers : []).map(t => safeMap(t, 'PROFESOR', 'TEACHER')),
      ...(Array.isArray(students) ? students : []).map(s => safeMap(s, 'ESTUDIANTE', 'STUDENT'))
    ].filter(u => u !== null);

    if (_INTERNAL_USER_REGISTRY_.length === 0) {
      const msg = diag !== "" ? `Error de carga: ${diag}` : "No se encontraron usuarios.";
      tbody.innerHTML = `<tr><td colspan="6" class="p-6 text-center text-gray-400">${msg}</td></tr>`;
    } else {
      renderAdminUsers(_INTERNAL_USER_REGISTRY_);
      if (diag !== "") toast(`Aviso: ${diag}`, "warning");
    }

  } catch (err) {
    console.error("Error procesando usuarios", err);
    tbody.innerHTML = `<tr><td colspan="6" class="p-6 text-center text-red-500 text-xs text-left"><pre>${err.message}\n${err.stack}</pre></td></tr>`;
  }
}

function renderAdminUsers(users) {
  const tbody = document.getElementById("admin-users-table-body");
  if (!tbody) return;

  if (users.length === 0) {
    tbody.innerHTML = '<tr><td colspan="6" class="p-6 text-center text-gray-400">No se encontraron usuarios.</td></tr>';
    return;
  }

  tbody.innerHTML = users.map(u => {
    const teacherAction = u.rawRole === 'TEACHER' 
      ? `<button class="text-blue-600 hover:text-blue-900 font-medium text-sm flex items-center gap-1" onclick="openTeacherDetailsModal('${u.uuid}', '${u.name}')" title="Perfil Docente">
          <i class="fas fa-chalkboard-user"></i> Perfil
         </button>` 
      : '';

    return `
      <tr class="hover:bg-gray-50 transition-colors">
        <td class="p-3 border-b font-mono text-xs text-gray-500">${u.id}</td>
        <td class="p-3 border-b font-medium text-gray-800">${u.name}</td>
        <td class="p-3 border-b text-gray-600">${u.email}</td>
        <td class="p-3 border-b">
          <span class="px-2 py-1 rounded-full text-[10px] font-bold uppercase ${u.rawRole === 'TEACHER' ? 'bg-indigo-100 text-indigo-700' : 'bg-blue-100 text-blue-700'}">
            ${u.role}
          </span>
        </td>
        <td class="p-3 border-b text-gray-400 font-mono text-xs tracking-widest">••••••••</td>
        <td class="p-3 border-b">
          <div class="flex items-center gap-3">
            ${teacherAction}
            <button class="text-purple-600 hover:text-purple-900 font-medium text-sm" onclick="toast('Edición global próximamente', 'info')" title="Editar">
              <i class="fas fa-user-edit"></i>
            </button>
            <button class="text-red-500 hover:text-red-700 font-medium text-sm" onclick="deleteAdminUser('${u.uuid}', '${u.name}')" title="Eliminar">
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </td>
      </tr>
    `;
  }).join("");
}

async function deleteAdminUser(uuid, name) {
  customConfirm(
    "¿Eliminar usuario?",
    `¿Estás seguro de que deseas eliminar permanentemente a "${name}"? Esta acción borrará su cuenta y sus datos asociados.`,
    async () => {
      try {
        const res = await fetch('/api/users/' + uuid, {
          method: 'DELETE',
          headers: {
            'Authorization': 'Bearer ' + rawAuth?.token
          }
        });
        
        if (res.ok || res.status === 204) {
          toast(`Usuario "${name}" eliminado exitosamente`, "success");
          loadAdminUsers();
        } else {
          const errData = await res.json().catch(() => null);
          toast(errData?.message || "Error al eliminar el usuario", "error");
        }
      } catch(err) {
        console.error(err);
        toast("Error de red", "error");
      }
    }
  );
}

window.deleteAdminUser = deleteAdminUser;

function filterUsersById() {
  const filterValue = document.getElementById("user-filter-id").value.toLowerCase();
  const filtered = _INTERNAL_USER_REGISTRY_.filter(u => u.id.toLowerCase().includes(filterValue));
  renderAdminUsers(filtered);
}
window.filterUsersById = filterUsersById;

// ─── RE-AUTHENTICATION LOGIC ───
function openReAuthModal() {
  document.getElementById("reauth-password").value = "";
  document.getElementById("modal-reauth").style.display = "flex";
  document.getElementById("reauth-password").focus();
}

function closeReAuthModal() {
  document.getElementById("modal-reauth").style.display = "none";
}

async function verifyReAuth() {
  const password = document.getElementById("reauth-password").value;
  if (!password) {
    toast("Ingresa tu contraseña para continuar", "warning");
    return;
  }

  try {
    const res = await fetch('/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: session.email,
        password: password,
        expectedRole: "ADMIN"
      })
    });

    const data = await res.json();
    if (data.success) {
      toast("Identidad verificada correctamente", "success");
      closeReAuthModal();
      openCreateUserModal();
    } else {
      toast("Contraseña incorrecta. Acceso denegado.", "error");
    }
  } catch (err) {
    toast("Error al verificar identidad", "error");
  }
}

// ─── USER CREATION LOGIC ───
function openCreateUserModal() {
  document.getElementById("create-user-name").value = "";
  document.getElementById("create-user-email").value = "";
  document.getElementById("create-user-password").value = "";
  document.getElementById("create-user-role").value = "STUDENT";
  document.getElementById("modal-create-user").style.display = "flex";
}

function closeCreateUserModal() {
  document.getElementById("modal-create-user").style.display = "none";
}

async function saveNewUser() {
  const name = document.getElementById("create-user-name").value.trim();
  const email = document.getElementById("create-user-email").value.trim();
  const password = document.getElementById("create-user-password").value;
  const role = document.getElementById("create-user-role").value;

  if (!name || !email || !password) {
    toast("Todos los campos son obligatorios", "warning");
    return;
  }
  if (password.length < 6) {
    toast("La contraseña debe tener al menos 6 caracteres", "warning");
    return;
  }

  const parts = name.split(" ");
  const firstName = parts[0];
  const lastName = parts.slice(1).join(" ") || "";

  const payload = {
    firstName,
    lastName,
    email,
    password,
    userType: role, // STUDENT, TEACHER, ADMIN
    userName: email.split("@")[0],
    // Generar IDs temporales si no existen
    studentId: role === 'STUDENT' ? "STU-" + Math.floor(Math.random() * 1000000) : null,
    teacherCode: role === 'TEACHER' ? "TEA-" + Math.floor(Math.random() * 1000000) : null,
    adminId: role === 'ADMIN' ? "ADM-" + Math.floor(Math.random() * 1000000) : null
  };

  try {
    const res = await fetch('/api/users', {
      method: 'POST',
      headers: {
        'Authorization': 'Bearer ' + rawAuth?.token,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });

    if (res.ok) {
      toast("¡Usuario creado con éxito!", "success");
      closeCreateUserModal();
      loadAdminUsers(); // Recargar tabla en tiempo real
    } else {
      const err = await res.json().catch(() => ({ message: "Error desconocido" }));
      toast(err.message || "No se pudo crear el usuario", "error");
    }
  } catch (e) {
    toast("Error de conexión", "error");
  }
}

window.loadAdminUsers = loadAdminUsers;
window.openReAuthModal = openReAuthModal;
window.closeReAuthModal = closeReAuthModal;
window.verifyReAuth = verifyReAuth;
window.openCreateUserModal = openCreateUserModal;
window.closeCreateUserModal = closeCreateUserModal;
window.saveNewUser = saveNewUser;

// ─── GRUPOS EN RIESGO ───
async function openAtRiskGroupsModal() {
  const tbody = document.getElementById("at-risk-groups-table-body");
  if (!tbody) return;
  tbody.innerHTML = '<tr><td colspan="5" class="p-6 text-center text-amber-600 animate-pulse">Identificando grupos bajo el umbral...</td></tr>';
  document.getElementById("modal-at-risk-groups").style.display = "flex";

  try {
    const [atRiskRes, teachersRes, subjectsRes] = await Promise.all([
      fetch('/api/course-groups/at-risk', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } }),
      fetch('/api/teachers', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } }),
      fetch('/api/subjects', { headers: { 'Authorization': 'Bearer ' + rawAuth?.token } })
    ]);

    const atRisk = atRiskRes.ok ? await atRiskRes.json() : [];
    const teachers = teachersRes.ok ? await teachersRes.json() : [];
    const subjects = subjectsRes.ok ? await subjectsRes.json() : [];

    if (atRisk.length === 0) {
      tbody.innerHTML = '<tr><td colspan="5" class="p-10 text-center text-gray-400 italic">No se encontraron grupos bajo el umbral de matrícula mínima. 🎉</td></tr>';
      return;
    }

    tbody.innerHTML = atRisk.map(cg => {
      const teacher = teachers.find(t => t.id === cg.teacherId || t.teacherId === cg.teacherId || t.teacherCode === cg.teacherId);
      const teacherName = teacher ? `${teacher.firstName} ${teacher.lastName}` : "Sin asignar";
      const subj = subjects.find(s => s.idSubject === cg.subjectId);
      const subjName = subj ? subj.subjectName : cg.subjectId;

      return `
        <tr class="hover:bg-amber-50 transition-colors">
          <td class="p-3 border-b font-mono text-xs text-gray-500">${cg.code || "N/A"}</td>
          <td class="p-3 border-b font-medium text-gray-800">${subjName}</td>
          <td class="p-3 border-b text-gray-600">${teacherName}</td>
          <td class="p-3 border-b text-center">
            <span class="bg-red-100 text-red-700 font-bold px-2 py-1 rounded-full text-xs">
              ${cg.enrolledCount || 0} alumnos
            </span>
          </td>
          <td class="p-3 border-b">
            <button onclick="processGroupClosure('${cg.courseGroupId}', '${subjName}')" 
                    class="bg-red-600 hover:bg-red-700 text-white text-xs font-bold py-1.5 px-3 rounded shadow transition-all">
              Cerrar Grupo
            </button>
          </td>
        </tr>
      `;
    }).join("");

  } catch (err) {
    console.error(err);
    tbody.innerHTML = '<tr><td colspan="5" class="p-6 text-center text-red-500">Error al cargar grupos en riesgo.</td></tr>';
  }
}

function closeAtRiskGroupsModal() {
  document.getElementById("modal-at-risk-groups").style.display = "none";
}

async function processGroupClosure(id, name) {
  customConfirm(
    "¿Cerrar grupo?",
    `¿Estás seguro de que deseas cerrar el grupo de "${name}"? Esta acción marcará el grupo como cerrado y deberás reasignar a los estudiantes manualmente.`,
    async () => {
      try {
        const res = await fetch(`/api/course-groups/${id}/close`, {
          method: 'POST',
          headers: { 'Authorization': 'Bearer ' + rawAuth?.token }
        });

        if (res.ok) {
          toast(`Grupo de "${name}" cerrado exitosamente.`, "success");
          openAtRiskGroupsModal(); // Refrescar modal
          loadAdminCourses();    // Refrescar tabla principal
        } else {
          toast("Error al cerrar el grupo.", "error");
        }
      } catch (err) {
        toast("Error de red.", "error");
      }
    }
  );
}

window.openAtRiskGroupsModal = openAtRiskGroupsModal;
window.closeAtRiskGroupsModal = closeAtRiskGroupsModal;
window.processGroupClosure = processGroupClosure;


