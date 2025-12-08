// This is the Create Card page
// It allows users to create a new card by filling out a form with the card's details.
// CreateCard.jsx — UH Design System (web components) versio
import React, { useEffect, useMemo, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

/* ---------- Rating-utils ---------- */
const toNum = (x, fallback) => {
  const n = Number(x);
  return Number.isFinite(n) ? n : fallback;
};
const makeRange = (min, max, step) => {
  const out = [];
  if (step <= 0) step = 1;
  for (let v = min; v <= max + 1e-9; v += step) {
    out.push(Math.round(v * 1000) / 1000);
  }
  return out;
};
const nearlyEq = (a, b) => Math.abs(Number(a) - Number(b)) < 1e-9;

/* ========== Pienet wrapperit ds-* tageille ========== */
function useWCEvent(ref, types, handler) {
  useEffect(() => {
    const el = ref.current;
    if (!el || !handler) return;
    const t = Array.isArray(types) ? types : [types];
    t.forEach((type) => el.addEventListener(type, handler));
    return () => t.forEach((type) => el.removeEventListener(type, handler));
  }, [ref, types, handler]);
}

const DSCard = ({ heading, description, children, ...rest }) => (
  <ds-card
    {...(heading ? { "ds-heading": heading } : {})}
    {...(description ? { "ds-description": description } : {})}
    {...rest}
    style={{ display: "block", marginBottom: 16 }}
  >
    <div style={{ padding: heading ? 8 : 0 }}>{children}</div>
  </ds-card>
);

const DSButton = ({
  variant = "primary",
  value,
  ariaLabel,
  icon,
  size = "medium",
  onClick,
  children,
  style,
  ...rest
}) => {
  const ref = useRef(null);
  useWCEvent(ref, "click", onClick);

  const childText =
    children === null ||
    children === undefined ||
    children === false ||
    children === true
      ? ""
      : String(children);
  const label = value ?? childText;

  return (
    <ds-button
      ref={ref}
      ds-variant={variant}
      ds-size={size}
      {...(icon ? { "ds-icon": icon } : {})}
      {...(label ? { "ds-value": label } : {})}
      aria-label={ariaLabel || label || icon || "button"}
      {...rest}
      style={{ marginRight: 8, marginBottom: 8, ...(style || {}) }}
    />
  );
};

const DSTextInput = ({
  id,
  label,
  value = "",
  onChange,
  placeholder,
  disabled,
  type,
  inputMode,
  style,
  ...rest
}) => {
  const ref = useRef(null);
  const [localValue, setLocalValue] = useState(value);

  useEffect(() => setLocalValue(value ?? ""), [value]);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    if (label != null) el.setAttribute("ds-label", String(label));
    else el.removeAttribute("ds-label");
    if (placeholder != null) el.setAttribute("placeholder", String(placeholder));
    else el.removeAttribute("placeholder");
    if (type) el.setAttribute("type", String(type));
    else el.removeAttribute("type");
    if (inputMode) el.setAttribute("inputmode", String(inputMode));
    else el.removeAttribute("inputmode");
    if (disabled) el.setAttribute("disabled", "");
    else el.removeAttribute("disabled");
    el.setAttribute("ds-value", localValue ?? "");
    el.value = localValue ?? "";
  }, [label, placeholder, disabled, localValue, type, inputMode]);

  useWCEvent(ref, ["input"], (e) => {
    const v = e.target?.value ?? "";
    setLocalValue(v);
  });

  useWCEvent(ref, ["blur", "ds-change", "change"], () => {
    onChange?.({ target: { value: localValue } });
  });

  return <ds-text-input id={id} ref={ref} style={style} {...rest} />;
};

const DSTextArea = ({
  id,
  label,
  value = "",
  placeholder,
  rows = 3,
  disabled,
  onChange,
  style,
}) => {
  const ref = useRef(null);
  const [localValue, setLocalValue] = useState(value);

  useEffect(() => setLocalValue(value ?? ""), [value]);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    if (label != null) el.setAttribute("ds-label", String(label));
    else el.removeAttribute("ds-label");
    if (placeholder != null)
      el.setAttribute("ds-placeholder", String(placeholder));
    else el.removeAttribute("ds-placeholder");
    if (disabled) el.setAttribute("disabled", "");
    else el.removeAttribute("disabled");
    el.setAttribute("ds-value", localValue ?? "");
    el.value = localValue ?? "";
  }, [label, placeholder, disabled, localValue]);

  useWCEvent(ref, ["input"], (e) => {
    const v = e.target?.value ?? "";
    setLocalValue(v);
  });

  useWCEvent(ref, ["blur", "change", "ds-change"], () => {
    onChange?.({ target: { value: localValue } });
  });

  return <ds-text-area id={id} ref={ref} rows={rows} style={style} />;
};

const DSCheckbox = ({ id, text, checked, onChange, disabled }) => {
  const ref = useRef(null);
  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    el.setAttribute("ds-text", text ?? "");
    el.setAttribute("ds-checked", checked ? "true" : "false");
    if (disabled) el.setAttribute("disabled", "");
    else el.removeAttribute("disabled");
  }, [text, checked, disabled]);
  useWCEvent(ref, ["change", "ds-change", "input"], () => {
    const el = ref.current;
    const next =
      el?.getAttribute("ds-checked") === "true" || el?.checked === true;
    onChange?.({ target: { checked: next } });
  });
  return <ds-checkbox id={id} ref={ref}></ds-checkbox>;
};

const DSRadio = ({ id, name, value, text, checked, onChange, disabled }) => {
  const ref = useRef(null);
  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    el.setAttribute("ds-text", text ?? "");
    el.setAttribute("ds-value", value ?? "");
    el.setAttribute("ds-checked", checked ? "true" : "false");
    if (disabled) el.setAttribute("disabled", "");
    else el.removeAttribute("disabled");
  }, [text, value, checked, disabled]);
  useWCEvent(ref, ["change", "ds-change"], () => {
    const el = ref.current;
    const isOn =
      el?.getAttribute("ds-checked") === "true" || el?.checked === true;
    if (isOn) onChange?.({ target: { value } });
  });
  return <ds-radio-button id={id} name={name} ref={ref}></ds-radio-button>;
};

const DSRadioGroup = ({
  label,
  assistiveText,
  direction = "vertical",
  required = false,
  children,
}) => (
  <ds-radio-button-group
    ds-label={label || ""}
    ds-assistive-text={assistiveText || ""}
    ds-direction={direction}
    {...(required ? { "ds-required": "" } : {})}
    style={{ display: "block" }}
  >
    {children}
  </ds-radio-button-group>
);

const DSCombobox = ({
  id,
  ariaLabel,
  value,
  onChange,
  options = [],
  placeholder,
  disabled,
}) => {
  const ref = useRef(null);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    if (ariaLabel) el.setAttribute("ds-aria-label", ariaLabel);
    if (placeholder) el.setAttribute("placeholder", placeholder);
    if (disabled) el.setAttribute("disabled", "");
    else el.removeAttribute("disabled");
  }, [ariaLabel, placeholder, disabled]);

  const optionNodes = options.map((o) => {
    const selected = String(value ?? "") === String(o.value);
    return (
      <ds-option
        key={o.value}
        ds-value={String(o.value)}
        ds-label={o.label}
        ds-selected={selected ? "true" : "false"}
      />
    );
  });

  useWCEvent(ref, ["change", "ds-change", "input"], () => {
    const el = ref.current;
    const valProp = el?.value;
    if (valProp != null) return onChange?.(valProp);
    const picked = [...(el?.querySelectorAll("ds-option") || [])].find(
      (n) => n.getAttribute("ds-selected") === "true"
    );
    onChange?.(picked?.getAttribute("ds-value") ?? "");
  });

  return (
    <ds-combobox id={id} ref={ref} ds-aria-label={ariaLabel || "Valitse"}>
      {optionNodes}
    </ds-combobox>
  );
};

/* ========================== Utilit ========================== */
const requiredEmpty = (v) => {
  if (v === undefined || v === null || v === "") return true;
  if (Array.isArray(v)) return v.length === 0;
  if (typeof v === "object") {
    return Object.values(v || {}).every(
      (x) => x === undefined || x === null || x === ""
    );
  }
  return false;
};

const uid = () => Math.random().toString(36).slice(2, 9);
function slugify(s) {
  return (
    farthestLeft(String(s || ""))
      .toLowerCase()
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "")
      .replace(/[^a-z0-9]+/g, "-")
      .replace(/^-+|-+$/g, "") || "opt"
  );
  function farthestLeft(x) {
    return x;
  }
}

/* dd/mm/yyyy */
const maskDDMMYYYY = (raw) => {
  const d = String(raw).replace(/\D/g, "").slice(0, 8);
  return [d.slice(0, 2), d.slice(2, 4), d.slice(4, 8)]
    .filter(Boolean)
    .join("/");
};
const clampDDMM = (str) => {
  const m = /^(\d{2})\/(\d{2})\/(\d{4})$/.exec(str || "");
  if (!m) return str;
  let dd = Math.max(1, Math.min(31, parseInt(m[1], 10) || 1));
  let mm = Math.max(1, Math.min(12, parseInt(m[2], 10) || 1));
  return `${String(dd).padStart(2, "0")}/${String(mm).padStart(
    2,
    "0"
  )}/${m[3]}`;
};
const isDDMMYYYY = (s) =>
  /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/.test(s || "");

/* ========================== Määrittelyt ========================== */
const ALL_ROLES = ["oppilas", "opettaja"];
const FIELD_TYPES = [
  { type: "text", label: "Otsikko" },
  { type: "textarea", label: "Kommenttiboksi" },
  { type: "select", label: "Pudotusvalikko" },
  { type: "checkboxes", label: "Monivalinta" },
  { type: "radio", label: "Valintanapit" },
  { type: "deadline", label: "Deadline" },
  { type: "number", label: "Numero" },
  { type: "rating", label: "Arviointiasteikko" },
  { type: "tooth", label: "Hammaskenttä" }, // UUSI
];

const defaultCard = {
  id: uid(),
  title: "Uusi suoritekortti",
  description: "Lisää kenttiä ja muokkaa ominaisuuksia.",
  fields: [],
  meta: { course: "", version: "1.0.0", visibility: "private" },
};

/* ========================== Validointi ========================== */
const validateCard = (card, data) => {
  const errs = {};
  for (const f of card.fields) {
    const v = data[f.id];
    if (f.type === "deadline" && !requiredEmpty(v) && !isDDMMYYYY(v)) {
      errs[f.id] = "Päivämäärä muodossa dd/mm/yyyy";
    }
    // Ei enää pakollisuuslogiikkaa
  }
  return errs;
};

/* ========================== Komponentti ========================== */
export default function CreateCard() {
  const navigate = useNavigate();
  const [card, setCard] = useState(defaultCard);
  const [selectedId, setSelectedId] = useState(null);
  const [previewData, setPreviewData] = useState({});
  const [isPreviewOpen, setPreviewOpen] = useState(false);
  const [previewRole, setPreviewRole] = useState("opettaja");
  const [errors, setErrors] = useState({});
  const fileInputRef = useRef(null);

  useEffect(() => {
    const body = document?.body;
    if (!body) return;
    body.classList.toggle("modal-open", isPreviewOpen);
    return () => body.classList.remove("modal-open");
  }, [isPreviewOpen]);

  const selectedField = useMemo(
    () => card.fields.find((f) => f.id === selectedId) || null,
    [card.fields, selectedId]
  );

  const canEdit = (field, role) =>
    Array.isArray(field.editableBy)
      ? field.editableBy.includes(role)
      : true;

  /* ---------- Kenttäkirjasto ---------- */
  function addField(type) {
    const base = {
      id: uid(),
      type,
      label: FIELD_TYPES.find((t) => t.type === type)?.label || type,
      required: false,
      help: "",
      editableBy: ["opettaja"],
    };
    if (["select", "radio", "checkboxes"].includes(type)) {
      base.options = [
        { id: uid(), label: "Vaihtoehto 1", value: slugify("Vaihtoehto 1") },
        { id: uid(), label: "Vaihtoehto 2", value: slugify("Vaihtoehto 2") },
      ];
    }
    if (type === "rating") {
      base.min = 1;
      base.max = 5;
      base.step = 1;
    }
    if (type === "tooth") {
      base.teethOptions = [];
      base.surfaceOptions = [];
      base.stepOptions = [];
    }
    setCard((c) => ({ ...c, fields: [...c.fields, base] }));
    setSelectedId(base.id);
  }
  function removeField(id) {
    setCard((c) => ({ ...c, fields: c.fields.filter((f) => f.id !== id) }));
    if (selectedId === id) setSelectedId(null);
  }
  function moveField(id, dir) {
    setCard((c) => {
      const idx = c.fields.findIndex((f) => f.id === id);
      if (idx < 0) return c;
      const arr = [...c.fields];
      const j = Math.max(0, Math.min(arr.length - 1, idx + dir));
      const [it] = arr.splice(idx, 1);
      arr.splice(j, 0, it);
      return { ...c, fields: arr };
    });
  }

  function updateField(id, patch) {
    setCard((c) => {
      const next = {
        ...c,
        fields: c.fields.map((f) => (f.id === id ? { ...f, ...patch } : f)),
      };
      if (Object.prototype.hasOwnProperty.call(patch, "editableBy")) {
        console.log("updateField editableBy", {
          id,
          patchEditableBy: patch.editableBy,
          resultEditableBy: next.fields.find((f) => f.id === id)?.editableBy,
        });
      }
      return next;
    });
 
    const changedKeys = Object.keys(patch);
    if (changedKeys.some((k) => ["min", "max", "step"].includes(k))) {
      setPreviewData((d) => {
        const f = card.fields.find((x) => x.id === id) || {};
        const min = toNum(patch.min ?? f.min, 1);
        const max = toNum(patch.max ?? f.max, 5);
        const step = toNum(patch.step ?? f.step, 1);
        const cur = d[id];
        const vals = makeRange(Math.min(min, max), Math.max(min, max), step);
        const fallback = vals[0];
        const ok = vals.some((v) => nearlyEq(v, cur));
        return { ...d, [id]: ok ? cur : fallback };
      });
    }
  }

  // yleinen options-lista (select, radio, checkboxes)
  function updateOption(field, i, patch) {
    const next = [...(field.options || [])];
    const prev = next[i] || {};
    const nextLabel = patch.label !== undefined ? patch.label : prev.label;
    next[i] = { ...prev, ...patch, value: slugify(nextLabel) };
    updateField(field.id, { options: next });
  }
  function addOption(field) {
    const label = "Uusi";
    const next = [
      ...(field.options || []),
      { id: uid(), label, value: slugify(label) },
    ];
    updateField(field.id, { options: next });
  }
  function removeOption(field, optId) {
    const next = (field.options || []).filter((o) => o.id !== optId);
    updateField(field.id, { options: next });
  }
  function moveOption(field, i, dir) {
    const arr = [...(field.options || [])];
    const j = Math.max(0, Math.min(arr.length - 1, i + dir));
    const [it] = arr.splice(i, 1);
    arr.splice(j, 0, it);
    updateField(field.id, { options: arr });
  }

  // Hammas-työvaiheet – kolmen listan editorit
  function updateToothOption(field, key, i, patch) {
    const list = field[key] || [];
    const next = [...list];
    const prev = next[i] || {};
    const nextLabel = patch.label !== undefined ? patch.label : prev.label;
    next[i] = { ...prev, ...patch, value: slugify(nextLabel) };
    updateField(field.id, { [key]: next });
  }
  function addToothOption(field, key) {
    const label = "Uusi";
    const list = field[key] || [];
    const next = [...list, { id: uid(), label, value: slugify(label) }];
    updateField(field.id, { [key]: next });
  }
  function removeToothOption(field, key, optId) {
    const list = field[key] || [];
    const next = list.filter((o) => o.id !== optId);
    updateField(field.id, { [key]: next });
  }
  function moveToothOption(field, key, i, dir) {
    const list = field[key] || [];
    const arr = [...list];
    const j = Math.max(0, Math.min(arr.length - 1, i + dir));
    const [it] = arr.splice(i, 1);
    arr.splice(j, 0, it);
    updateField(field.id, { [key]: arr });
  }

  /* ---------- JSON vienti/tuonti ---------- */
  function exportJSON() {
    const blob = new Blob([JSON.stringify(card, null, 2)], {
      type: "application/json",
    });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "kortti.json";
    a.click();
    URL.revokeObjectURL(url);
  }
  function importJSON(file) {
    const reader = new FileReader();
    reader.onload = () => {
      try {
        const obj = JSON.parse(String(reader.result));
        if (!obj.fields) throw new Error("virheellinen");
        setCard(obj);
        setSelectedId(null);
      } catch {
        alert("Tiedoston lukeminen epäonnistui.");
      }
    };
    reader.readAsText(file);
  }

  /* ---------- Esikatselun kenttä ---------- */
  function renderPreviewField(f, role) {
    const readOnly = !canEdit(f, role);
    const setVal = (v) => {
      if (readOnly) return;
      setPreviewData((d) => ({ ...d, [f.id]: v }));
    };
    const err = errors[f.id];
    const showOuterLabel = !["text", "checkboxes", "radio"].includes(f.type);

    return (
      <div key={f.id} className="field">
        {showOuterLabel && (
          <>
            <div className="field-label">
              {f.label}{" "}
              {f.required && (
                <span aria-hidden="true" className="req">
                  *
                </span>
              )}
              {readOnly && (
                <span className="hint" style={{ marginLeft: 8 }}>
                  (vain luku)
                </span>
              )}
            </div>
            {f.help && <div className="hint">{f.help}</div>}
          </>
        )}

        {f.type === "text" && (
          <>
            <h3 className="title" style={{ margin: 0 }}>
              {f.label}
            </h3>
            {f.help && <div className="hint">{f.help}</div>}
          </>
        )}

        {f.type === "number" && (
          <DSTextInput
            id={`num-${f.id}`}
            type="number"
            value={previewData[f.id] ?? ""}
            onChange={(e) => setVal(e.target.value)}
            placeholder="0"
            disabled={readOnly}
          />
        )}

        {f.type === "textarea" && (
          <DSTextArea
            id={`ta-${f.id}`}
            value={previewData[f.id] ?? ""}
            placeholder="Kirjoita kommentti…"
            onChange={(e) => setVal(e.target.value)}
            disabled={readOnly}
          />
        )}

        {f.type === "deadline" && (
          <>
            <DSTextInput
              id={`deadline-${f.id}`}
              type="text"
              value={previewData[f.id] ?? ""}
              onChange={(e) => setVal(maskDDMMYYYY(e.target.value))}
              onBlur={(e) => {
                const v = e.target.value;
                if (v.length === 10 && isDDMMYYYY(clampDDMM(v)))
                  setVal(clampDDMM(v));
              }}
              placeholder="dd/mm/yyyy"
              disabled={readOnly}
            />
            <div className="hint">Muoto: dd/mm/yyyy</div>
          </>
        )}

        {f.type === "select" && (
          <DSCombobox
            id={`sel-${f.id}`}
            ariaLabel={f.label}
            value={previewData[f.id] ?? ""}
            options={(f.options || []).map((o) => ({
              value: o.value,
              label: o.label,
            }))}
            onChange={(val) => setVal(val)}
            disabled={readOnly}
          />
        )}

        {f.type === "radio" && (
          <>
            <DSRadioGroup
              label={f.label}
              assistiveText={f.help || ""}
              required={!!f.required}
            >
              {(f.options || []).map((o) => (
                <DSRadio
                  key={o.id}
                  id={`${f.id}-${o.id}`}
                  name={f.id}
                  value={o.value}
                  text={o.label}
                  checked={previewData[f.id] === o.value}
                  onChange={() => !readOnly && setVal(o.value)}
                  disabled={readOnly}
                />
              ))}
            </DSRadioGroup>
            {err && <div className="error">{err}</div>}
          </>
        )}

        {f.type === "checkboxes" && (
          <>
            <ds-checkbox-group
              ds-label={f.label}
              ds-assistive-text={f.help || ""}
              ds-direction="vertical"
              style={{ display: "block" }}
              {...(f.required ? { "ds-required": "" } : {})}
            >
              {(f.options || []).map((o) => {
                const sel = new Set(previewData[f.id] || []);
                const checked = sel.has(o.value);
                return (
                  <DSCheckbox
                    key={o.id}
                    id={`${f.id}-${o.id}`}
                    text={o.label}
                    checked={!!checked}
                    onChange={(e) => {
                      if (readOnly) return;
                      const cur = new Set(previewData[f.id] || []);
                      e.target.checked ? cur.add(o.value) : cur.delete(o.value);
                      setVal([...cur]);
                    }}
                    disabled={readOnly}
                  />
                );
              })}
            </ds-checkbox-group>
            {err && <div className="error">{err}</div>}
          </>
        )}

        {/* Hammas työvaiheet */}
        {f.type === "tooth" && (
          <>
            <div className="row">
              <div>
                <label className="small-label">Hammas</label>
                <DSCombobox
                  id={`tooth-${f.id}-hammas`}
                  ariaLabel="Hammas"
                  value={(previewData[f.id] || {}).tooth || ""}
                  options={(f.teethOptions || []).map((o) => ({
                    value: o.value,
                    label: o.label,
                  }))}
                  onChange={(val) =>
                    !readOnly &&
                    setPreviewData((d) => ({
                      ...d,
                      [f.id]: { ...(d[f.id] || {}), tooth: val },
                    }))
                  }
                  disabled={readOnly}
                />
              </div>
              <div>
                <label className="small-label">Pinta</label>
                <DSCombobox
                  id={`tooth-${f.id}-pinta`}
                  ariaLabel="Pinta"
                  value={(previewData[f.id] || {}).surface || ""}
                  options={(f.surfaceOptions || []).map((o) => ({
                    value: o.value,
                    label: o.label,
                  }))}
                  onChange={(val) =>
                    !readOnly &&
                    setPreviewData((d) => ({
                      ...d,
                      [f.id]: { ...(d[f.id] || {}), surface: val },
                    }))
                  }
                  disabled={readOnly}
                />
              </div>
              <div>
                <label className="small-label">Työvaihe</label>
                <DSCombobox
                  id={`tooth-${f.id}-tyovaihe`}
                  ariaLabel="Työvaihe"
                  value={(previewData[f.id] || {}).step || ""}
                  options={(f.stepOptions || []).map((o) => ({
                    value: o.value,
                    label: o.label,
                  }))}
                  onChange={(val) =>
                    !readOnly &&
                    setPreviewData((d) => ({
                      ...d,
                      [f.id]: { ...(d[f.id] || {}), step: val },
                    }))
                  }
                  disabled={readOnly}
                />
              </div>
            </div>
          </>
        )}

        {f.type === "rating" && (
          <div className="rating">
            {(() => {
              const min = toNum(f.min, 1);
              const max = toNum(f.max, 5);
              const step = toNum(f.step, 1) > 0 ? toNum(f.step, 1) : 1;
              const lo = Math.min(min, max);
              const hi = Math.max(min, max);
              const vals = makeRange(lo, hi, step);
              const current = previewData[f.id];

              return vals.map((v) => (
                <DSButton
                  key={v}
                  variant={nearlyEq(current, v) ? "primary" : "secondary"}
                  ariaLabel={`Arvo ${v}`}
                  value={String(v)}
                  onClick={() => !readOnly && setVal(v)}
                />
              ));
            })()}
          </div>
        )}
      </div>
    );
  }

  return (
     <div className="page" style={{ paddingBottom: "140px" }}>
        <div className="page-head">
        <div className="page-head-titles">
          <h1 className="h1">{card.title}</h1>
          <p className="subtle">{card.description}</p>
        </div>

       <div className="page-head-actions">
          <DSButton onClick={exportJSON}>Vie JSON</DSButton>
          <DSButton
            onClick={() => fileInputRef.current?.click()}
          >
            Tuo JSON
          </DSButton>

          <input
            ref={fileInputRef}
            type="file"
            accept="application/json"
            style={{ display: "none" }}
            onChange={(e) => {
              const f = e.target.files?.[0];
              if (f) importJSON(f);
              e.target.value = "";
            }}
          />
        </div>
      </div>

      <hr className="section-divider" />

      {/* Kenttäkirjasto */}
      <DSCard>
        <h2 style={{ marginTop: 0 }}>Kenttäkirjasto</h2>
        <div className="chip-grid">
          {FIELD_TYPES.map((ft) => (
            <DSButton
              key={ft.type}
              icon="add"
              ariaLabel={`Lisää: ${ft.label}`}
              onClick={() => addField(ft.type)}
            >
              {ft.label}
            </DSButton>
          ))}
        </div>
      </DSCard>

      <hr className="section-divider" />

      {/* Kenttälista */}
      <DSCard>
        <h2 style={{ marginTop: 0 }}>Kentät</h2>
        {card.fields.length === 0 && (
          <div className="empty">Lisää kenttiä kirjastosta.</div>
        )}

        {card.fields.map((f) => (
          <div
            key={f.id}
            onClick={() => setSelectedId(f.id)}
            className={`row-card ${selectedId === f.id ? "selected" : ""}`}
            role="button"
          >
            <div>
              <strong>{f.label}</strong>{" "}
              <span className="muted">({f.type})</span>
            </div>
            <div className="field-buttons">
              <DSButton
                ariaLabel="Siirrä ylös"
                icon="arrow_upward"
                onClick={(e) => {
                  e.stopPropagation();
                  moveField(f.id, -1);
                }}
              />
              <DSButton
                ariaLabel="Siirrä alas"
                icon="arrow_downward"
                onClick={(e) => {
                  e.stopPropagation();
                  moveField(f.id, 1);
                }}
              />
              <DSButton
                variant="secondary"
                ariaLabel="Poista kenttä"
                icon="delete"
                onClick={(e) => {
                  e.stopPropagation();
                  removeField(f.id);
                }}
              />
            </div>
          </div>
        ))}
      </DSCard>

      <hr className="section-divider" />

      {/* Ominaisuudet */}
      <DSCard>
        <h2 style={{ marginTop: 0 }}>Ominaisuudet</h2>
        {!selectedField && (
          <div className="empty">Valitse kenttä yllä olevasta listasta.</div>
        )}

        {selectedField && (
          <>
            <label className="small-label">Otsikko</label>
            <DSTextInput
              key={`label-editor-${selectedField.id}`}
              id={`field-label-${selectedField.id}`}
              value={selectedField.label}
              onChange={(e) =>
                updateField(selectedField.id, { label: e.target.value })
              }
            />

            <label className="small-label">Ohjeteksti</label>
            <DSTextArea
              key={`help-editor-${selectedField.id}`}
              id={`field-help-${selectedField.id}`}
              value={selectedField.help || ""}
              onChange={(e) =>
                updateField(selectedField.id, { help: e.target.value })
              }
            />


            {/* Kuka voi muokata */}
            <div className="block">
              <strong>Kuka voi muokata tätä kenttää?</strong>
              <div className="inline-wrap">
                {ALL_ROLES.map((r) => {
                  const checked = (selectedField.editableBy || [
                    "opettaja",
                  ]).includes(r);
                  return (
                    <label 
                      key={r} 
                      className="inline"
                      style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '8px',
                        cursor: 'pointer',
                        padding: '6px 12px',
                        borderRadius: '8px',
                        transition: 'background-color 0.2s',
                        backgroundColor: checked ? 'hsl(210 40% 96.1%)' : 'transparent',
                      }}
                    >
                      <input
                        type="checkbox"
                        checked={checked}
                        onChange={(e) => {
                          const current = new Set(
                            selectedField.editableBy || ["opettaja"]
                          );
                          e.target.checked
                            ? current.add(r)
                            : current.delete(r);
                          const arr = [...current];
                          updateField(selectedField.id, {
                            editableBy: arr.length ? arr : ["opettaja"],
                          });
                        }}
                        style={{
                          width: '18px',
                          height: '18px',
                          cursor: 'pointer',
                          accentColor: 'hsl(222.2 47.4% 11.2%)',
                        }}
                      />
                      <span style={{ textTransform: "capitalize", fontWeight: checked ? 500 : 400 }}>{r}</span>
                    </label>
                  );
                })}
              </div>
            </div>

            {/* Select / radio / checkboxes -vaihtoehdot */}
            {["select", "radio", "checkboxes"].includes(
              selectedField.type
            ) && (
              <>
                <div className="row space">
                  <strong>Vaihtoehdot</strong>
                  <DSButton onClick={() => addOption(selectedField)}>
                    + Lisää vaihtoehto
                  </DSButton>
                </div>

                {(selectedField.options || []).map((o, i) => (
                  <div
                    key={o.id}
                    className="choice-row"
                    style={{ marginTop: 6 }}
                  >
                    <DSTextInput
                      key={`opt-${selectedField.id}-${o.id}`}
                      id={`opt-${selectedField.id}-${o.id}`}
                      value={o.label}
                      onChange={(e) =>
                        updateOption(selectedField, i, {
                          label: e.target.value,
                        })
                      }
                      placeholder="Vaihtoehdon nimi"
                    />
                    <DSButton
                      ariaLabel="Ylös"
                      icon="arrow_upward"
                      size="small"
                      onClick={() =>
                        moveOption(selectedField, i, -1)
                      }
                    />
                    <DSButton
                      ariaLabel="Alas"
                      icon="arrow_downward"
                      size="small"
                      onClick={() =>
                        moveOption(selectedField, i, 1)
                      }
                    />
                    <DSButton
                      ariaLabel="Poista"
                      icon="delete"
                      size="small"
                      variant="secondary"
                      onClick={() =>
                        removeOption(selectedField, o.id)
                      }
                    />
                    <hr
                      style={{
                        width: "100%",
                        borderTop: "1px solid #eee",
                      }}
                    />
                  </div>
                ))}
              </>
            )}

            {/* Hammas työvaiheet – kolme listaa */}
            {selectedField.type === "tooth" && (
              <>
                {[
                  ["teethOptions", "Hammas"],
                  ["surfaceOptions", "Pinta"],
                  ["stepOptions", "Työvaihe"],
                ].map(([key, title]) => (
                  <div key={key} style={{ marginTop: 12 }}>
                    <div className="row space">
                      <strong>{title}</strong>
                      <DSButton
                        onClick={() =>
                          addToothOption(selectedField, key)
                        }
                      >
                        + Lisää vaihtoehto
                      </DSButton>
                    </div>
                    {(selectedField[key] || []).map((o, i) => (
                      <div
                        key={o.id}
                        className="choice-row"
                        style={{ marginTop: 6 }}
                      >
                        <DSTextInput
                          id={`${key}-${selectedField.id}-${o.id}`}
                          value={o.label}
                          onChange={(e) =>
                            updateToothOption(
                              selectedField,
                              key,
                              i,
                              { label: e.target.value }
                            )
                          }
                          placeholder="Vaihtoehdon nimi"
                        />
                        <DSButton
                          ariaLabel="Ylös"
                          icon="arrow_upward"
                          size="small"
                          onClick={() =>
                            moveToothOption(selectedField, key, i, -1)
                          }
                        />
                        <DSButton
                          ariaLabel="Alas"
                          icon="arrow_downward"
                          size="small"
                          onClick={() =>
                            moveToothOption(selectedField, key, i, 1)
                          }
                        />
                        <DSButton
                          ariaLabel="Poista"
                          icon="delete"
                          size="small"
                          variant="secondary"
                          onClick={() =>
                            removeToothOption(selectedField, key, o.id)
                          }
                        />
                        <hr
                          style={{
                            width: "100%",
                            borderTop: "1px solid #eee",
                          }}
                        />
                      </div>
                    ))}
                  </div>
                ))}
              </>
            )}

            {/* Rating – vain numeroväli */}
            {selectedField.type === "rating" && (
              <>
                <div className="row">
                  <div>
                    <label className="small-label">Min</label>
                    <DSTextInput
                      key={`min-${selectedField.id}`}
                      id={`min-${selectedField.id}`}
                      type="number"
                      value={selectedField.min}
                      onChange={(e) =>
                        updateField(selectedField.id, {
                          min: Number(e.target.value),
                        })
                      }
                    />
                  </div>
                  <div>
                    <label className="small-label">Max</label>
                    <DSTextInput
                      key={`max-${selectedField.id}`}
                      id={`max-${selectedField.id}`}
                      type="number"
                      value={selectedField.max}
                      onChange={(e) =>
                        updateField(selectedField.id, {
                          max: Number(e.target.value),
                        })
                      }
                    />
                  </div>
                  <div>
                    <label className="small-label">Askellus</label>
                    <DSTextInput
                      key={`step-${selectedField.id}`}
                      id={`step-${selectedField.id}`}
                      type="number"
                      value={selectedField.step}
                      onChange={(e) =>
                        updateField(selectedField.id, {
                          step: Number(e.target.value),
                        })
                      }
                    />
                  </div>
                </div>
              </>
            )}

            <div className="row space" style={{ marginTop: 12 }}>
              <DSButton
                onClick={() => {
                  setPreviewRole("opettaja");
                  setPreviewOpen(true);
                }}
              >
                Koko kortin esikatselu
              </DSButton>
            </div>

            <hr />
            <h3 className="h3">Esikatselu (opettaja)</h3>
            {card.fields
              .filter((f) => f.id === selectedField.id)
              .map((f) => renderPreviewField(f, "opettaja"))}
          </>
        )}
      </DSCard>

      {/* Modali */}
      {isPreviewOpen && (
        <div className="modal" role="dialog" aria-modal="true">
          <div className="modal-content">
            <div className="modal-head">
              <h3 style={{ marginRight: "auto" }}>
                {card.title || "Kortin esikatselu"}
              </h3>
            <div className="inline">
              <strong>Rooli:</strong>
              <select
                value={previewRole}
                onChange={(e) => setPreviewRole(e.target.value)}
                style={{
                  minWidth: 160,
                  padding: "10px 14px",
                  border: "1px solid hsl(214.3 31.8% 91.4%)",
                  borderRadius: "8px",
                  fontSize: "16px",
                  backgroundColor: "hsl(0 0% 100%)",
                  color: "hsl(222.2 84% 4.9%)",
                  cursor: "pointer",
                  outline: "none",
                  transition: "all 0.2s",
                }}
                onFocus={(e) => {
                  e.target.style.borderColor = "hsl(222.2 47.4% 11.2%)";
                  e.target.style.boxShadow = "0 0 0 3px hsl(222.2 47.4% 11.2% / 0.1)";
                }}
                onBlur={(e) => {
                  e.target.style.borderColor = "hsl(214.3 31.8% 91.4%)";
                  e.target.style.boxShadow = "none";
                }}
              >
                <option value="oppilas">Oppilas</option>
                <option value="opettaja">Opettaja</option>
              </select>
            </div>
              <DSButton
                variant="secondary"
                className="modal-close"
                icon="close"
                ariaLabel="Sulje"
                onClick={() => setPreviewOpen(false)}
              />
            </div>

            {card.description &&
              card.description !== "Lisää kenttiä ja muokkaa ominaisuuksia." && (
                <p className="subtle" style={{ marginBottom: 12 }}>
                  {card.description}
                </p>
              )}

            <form
              onSubmit={(e) => {
                e.preventDefault();
                const errs = validateCard(card, previewData);
                setErrors(errs);
                if (Object.keys(errs).length === 0) {
                  alert("OK! (demo) – tässä kohtaa tallennetaan palvelimelle.");
                  setPreviewOpen(false);
                }
              }}
            >
              {card.fields.map((f) => (
                <div key={f.id} className="field">
                  {renderPreviewField(f, previewRole)}
                  {errors[f.id] && (
                    <div className="error">{errors[f.id]}</div>
                  )}
                </div>
              ))}

              <div className="modal-actions">
                <DSButton
                  variant="secondary"
                  type="button"
                  onClick={() => setPreviewOpen(false)}
                >
                  Sulje
                </DSButton>
                <DSButton type="submit">Tallenna (demo)</DSButton>
              </div>
            </form>
          </div>
          {/* ---------- ALAPALKKI: Takaisin / Tallenna ---------- */}
<div style={footerStyles.footerBar}>
  <div style={footerStyles.footerInner}>
    <DSButton
      variant="secondary"
      onClick={() => navigate(-1)}     // palaa edelliselle sivulle
    >
      Takaisin
    </DSButton>

    <DSButton
      variant="primary"
      onClick={() => {
        // halutessasi lisää oikea tallennus
        alert("Luonnos tallennettu");
      }}
    >
      Tallenna luonnos
    </DSButton>
  </div>
</div>

        </div>
      )}

      {/* Kevyt layout-tyyli :-D */}
      <style>{`
        .page { max-width: 960px; margin: 0 auto; padding: 16px; }
        .subtle { color:#4b5563; font-size:14px; margin-top:4px; }
        .muted { color:#6b7280; }
        .h1 { font-size:22px; margin: 4px 0 8px; }
        .h3 { font-size:18px; margin: 8px 0; }

        .chip-grid { display:flex; flex-wrap:wrap; gap:8px; }
        .empty { padding:12px; border:1px dashed #d1d5db; border-radius:8px; color:#6b7280; }
        .row { display:flex; gap:8px; align-items:center; flex-wrap:wrap; }
        .row.space { justify-content:space-between; }
        .row-card { display:flex; align-items:center; justify-content:space-between; padding:10px 12px; border:1px solid #b3b3b3; border-radius:10px; margin-bottom:8px; background:#e6e6e6; transition: background .15s ease; }
        .row-card.selected { background:#cccccc; border-color:#808080; }

        .small-label { font-size:12px; color:#374151; display:block; margin:6px 0 4px; }
        .inline { display:inline-flex; align-items:center; gap:8px; }
        .inline-wrap { display:flex; gap:16px; flex-wrap:wrap; margin-top:6px; }
        .block { margin-top:10px; }

        .field { margin-bottom:14px; }
        .field-label { display:block; font-weight:600; margin-bottom:4px; }
        .hint { font-size:12px; color:#6b7280; margin:4px 0; }
        .req { color:#b91c1c; }
        .error { color:#b91c1c; margin-top:6px; }

        .rating { display:flex; gap:6px; flex-wrap:wrap; }
        .signature { height:80px; border:2px dashed #c7cdd4; border-radius:8px; display:flex; align-items:center; justify-content:center; color:#6b7280; }

        .choice-row { display:grid; grid-template-columns: 1fr 32px 32px 40px; align-items:center; gap:8px; margin-top:6px; }

        /* Modal */
        .modal { position:fixed; inset:0; background:rgba(0,0,0,.35); display:grid; place-items:center; padding:16px; z-index:10000; }
        .modal-content { width:min(780px, 100%); max-height:90vh; overflow:auto; background:#fff; border-radius:12px; padding:16px; }
        .modal-head { display:flex; align-items:center; gap:12px; margin-bottom:8px; }
        .modal-actions { display:flex; justify-content:flex-end; gap:8px; margin-top:8px; }
        .modal-close { font-size:18px; line-height:1; padding:4px 8px; }

        body, html { background:#f2f2f2; display:flex; align-items:flex-start; justify-content:center; min-height:100vh; margin:0; color:#000; }
        .page { max-width: 960px; width:100%; background:#fafafa; border-radius:12px; box-shadow:0 4px 10px rgba(0,0,0,0.1); padding:24px; margin-top:32px; }

        h1, h2, h3 { color:#00264d; }
        .subtle { color:#444; }

        body.modal-open { overflow: hidden; }

        .page-head { display:flex; align-items:center; justify-content:space-between; gap:16px; flex-wrap:wrap; }
        .page-head-actions { display:inline-flex; gap:8px; }

        .section-divider { border:0; border-top:1px solid #cbd5e1; margin:8px 0 16px; }
      `}</style>
         {/* ---------- ALAPALKKI: Takaisin / Tallenna ---------- */}
      <div style={footerStyles.footerBar}>
        <div style={footerStyles.footerInner}>
          <DSButton variant="secondary" onClick={() => navigate(-1)}>
            Takaisin
          </DSButton>

          <DSButton variant="primary" onClick={() => alert("Luonnos tallennettu")}>
            Tallenna luonnos
          </DSButton>
        </div>
      </div>
    </div>
  );
}
const footerStyles = {
  footerBar: {
    position: "fixed",
    left: 0,
    right: 0,
    bottom: 55,                        
    padding: "8px 16px",
    backgroundColor: "#fafafa",
    borderTop: "1px solid #cbd5e1",
    zIndex: 1000,
    boxSizing: "border-box",
  },
  footerInner: {
    maxWidth: 960,
    margin: "0 auto",
    display: "flex",
    justifyContent: "space-between",
    gap: 8,
  },
};