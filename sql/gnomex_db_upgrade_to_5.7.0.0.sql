-- Create missing PropertyEntryOption entries for sample annotations of type 'OPTION'
insert into PropertyEntryOption
(idPropertyEntry, idPropertyOption)
select pe.idPropertyEntry, pe.valueString as idPropertyOption
from PropertyEntry pe
  left join Property p
  on p.idProperty = pe.idProperty
  join PropertyOption po
  on po.idPropertyOption = pe.valueString
where idSample is not null
and codePropertyType = 'OPTION'
